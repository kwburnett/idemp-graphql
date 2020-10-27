package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.cache.BandaCache;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.utils.FilterUtil;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.bandahealth.idempiere.graphql.utils.SortUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRepository<T extends PO, S extends T> {

	public final BandaCache<Object, Object> cache;
	protected final CLogger logger;
	protected final String PURCHASE_ORDER = "Purchase Order";

	public BaseRepository() {
		Class<?> childClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		cache = GraphQLEndpoint.getCache(childClass);
		logger = CLogger.getCLogger(childClass);
	}

	public abstract T getModelInstance();

	public abstract T save(S entity);

	/**
	 * Override this in child classes if the client ID in the context should not be added by default
	 *
	 * @return
	 */
	protected boolean shouldUseContextClientId() {
		return true;
	}

	/**
	 * This should be overridden in inheriting classes.
	 * Structure: Map<TableName, JOIN clause>
	 *
	 * @return A map of table names and their appropriate JOIN clauses
	 */
	public Map<String, String> getDynamicJoins() {
		return new HashMap<>();
	}

	/**
	 * Get a list of this entity grouped by IDs
	 *
	 * @param groupingFunction The grouping function to apply for these entities
	 * @param columnToSearch   The search column to check in
	 * @param ids              The IDs to search by
	 * @return
	 */
	public CompletableFuture<Map<Integer, List<T>>> getGroupsByIdsCompletableFuture(
			Function<T, Integer> groupingFunction, String columnToSearch, Set<Integer> ids) {
		return CompletableFuture.supplyAsync(() -> getGroupsByIds(groupingFunction, columnToSearch, ids));
	}

	/**
	 * Get a list of this entity grouped by IDs
	 *
	 * @param groupingFunction The grouping function to apply for these entities
	 * @param columnToSearch   The search column to check in
	 * @param ids              The IDs to search by
	 * @return
	 */
	public Map<Integer, List<T>> getGroupsByIds(
			Function<T, Integer> groupingFunction, String columnToSearch, Set<Integer> ids) {
		T model = getModelInstance();
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		List<T> models = new Query(Env.getCtx(), model.get_TableName(),
				columnToSearch + " IN (" + whereCondition + ")", null)
				.setParameters(parameters).list();
		return models.stream().collect(Collectors.groupingBy(groupingFunction));
	}

	public T getById(int id) {
		T model = getModelInstance();
		return new Query(Env.getCtx(), model.get_TableName(), model.get_TableName() + "_ID=?", null)
				.setParameters(id).first();
	}

	/**
	 * Get a list of entities by their IDs
	 *
	 * @param ids The IDs to search by
	 * @return
	 */
	public Map<Integer, T> getByIds(Set<Integer> ids) {
		T model = getModelInstance();
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		List<T> models = new Query(Env.getCtx(), model.get_TableName(),
				model.get_TableName() + "_ID IN (" + whereCondition + ")", null)
				.setParameters(parameters).list();
		return models.stream().collect(Collectors.toMap(T::get_ID, m -> m));
	}

	/**
	 * Get a list of entities by their IDs
	 *
	 * @param ids The IDs to search by
	 * @return
	 */
	public CompletableFuture<Map<Integer, T>> getByIdsCompletableFuture(Set<Integer> ids) {
		return CompletableFuture.supplyAsync(() -> getByIds(ids));
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuid The UUID to search by
	 * @return
	 */
	public T getByUuid(String uuid) {
		T model = getModelInstance();
		return new Query(Env.getCtx(), model.get_TableName(),
				model.getUUIDColumnName() + "=?", null)
				.setParameters(uuid).first();
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuids The UUIDs to search by
	 * @return
	 */
	public List<T> getByUuids(List<String> uuids) {
		T model = getModelInstance();
		String whereClause = uuids.stream().filter(uuid -> !StringUtil.isNullOrEmpty(uuid)).map(uuid -> "'" + uuid + "'")
				.collect(Collectors.joining(","));
		return new Query(Env.getCtx(), model.get_TableName(),
				model.getUUIDColumnName() + " IN (" + whereClause + ")", null)
				.list();
	}

	public Connection<T> get(String filterJson, String sort, PagingInfo pagingInfo, String whereClause,
			List<Object> parameters, DataFetchingEnvironment environment) {
		return this.get(filterJson, sort, pagingInfo, whereClause, parameters, null, environment);
	}

	/**
	 * Get all with the inclusion of a join clause for joined cases of sorting
	 *
	 * @param filterJson
	 * @param sortJson
	 * @param pagingInfo
	 * @param whereClause
	 * @param parameters
	 * @param joinClause  Use to specify a linked table so joining can occur
	 * @return
	 */
	public Connection<T> get(String filterJson, String sortJson, PagingInfo pagingInfo, String whereClause,
			List<Object> parameters, String joinClause, DataFetchingEnvironment environment) {
		try {
			if (parameters == null) {
				parameters = new ArrayList<>();
			}
			T modelInstance = getModelInstance();

			String filterWhereClause = FilterUtil.getWhereClauseFromFilter(modelInstance, filterJson, parameters);
			if (StringUtil.isNullOrEmpty(whereClause)) {
				whereClause = filterWhereClause;
			} else {
				whereClause += " AND " + filterWhereClause;
			}

			Query query = new Query(Env.getCtx(), modelInstance.get_TableName(), whereClause, null)
					.setNoVirtualColumn(true);
			if (shouldUseContextClientId()) {
				query.setClient_ID();
			}

			StringBuilder dynamicJoinClause = new StringBuilder();
			if (!getDynamicJoins().isEmpty()) {
				String passedInJoinClause = (joinClause == null ? "" : joinClause).toLowerCase();
				List<String> neededJoinTables = FilterUtil.getTablesNeedingJoins(filterJson);
				neededJoinTables.addAll(SortUtil.getTablesNeedingJoins(sortJson));
				neededJoinTables = neededJoinTables.stream().distinct().collect(Collectors.toList());
				for (String tableNeedingJoin : neededJoinTables) {
					// If this table was already specified in a JOIN, we don't need to dynamically add it
					if (passedInJoinClause.contains(tableNeedingJoin + ".")) {
						continue;
					}
					// Find the needed JOIN clause
					boolean foundMatchForTable = false;
					for (String dynamicTableJoinName : getDynamicJoins().keySet()) {
						if (dynamicTableJoinName.equalsIgnoreCase(tableNeedingJoin)) {
							dynamicJoinClause.append(" ").append(getDynamicJoins().get(dynamicTableJoinName));
							foundMatchForTable = true;
						}
					}
					// If no JOIN clause is specified in the dynamic JOIN, we need to let the user know
					if (!foundMatchForTable) {
						throw new AdempiereException(tableNeedingJoin
								+ " was specified in the filter/sort, but no dynamic JOIN clause provided");
					}
				}
			}
			if (joinClause != null) {
				dynamicJoinClause.append(" ").append(joinClause);
			}

			if (!dynamicJoinClause.toString().trim().isEmpty()) {
				query.addJoinClause(dynamicJoinClause.toString().trim());
			}

			String orderBy = SortUtil.getOrderByClauseFromSort(modelInstance, sortJson);
			if (orderBy != null) {
				query = query.setOrderBy(orderBy);
			}

			if (parameters.size() > 0) {
				query = query.setParameters(parameters);
			}

			if (QueryUtil.isTotalCountRequested(environment)) {
				// get total count without pagination parameters
				pagingInfo.setTotalCount(query.count());
			}

			// set pagination params
			query = query.setPage(pagingInfo.getPageSize(), pagingInfo.getPage());
			return new Connection<>(query.list(), pagingInfo);
		} catch (Exception ex) {
			throw new AdempiereException(ex);
		}
	}
}
