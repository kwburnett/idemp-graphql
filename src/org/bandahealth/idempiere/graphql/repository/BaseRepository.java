package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.cache.BandaCache;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.utils.*;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The base repository that holds logic common to all repositories.
 *
 * @param <T> The iDempiere model that is used for DB interactions.
 * @param <S> The input model for updates, if the iDempiere model itself isn't used.
 */
public abstract class BaseRepository<T extends PO, S extends T> {

	public final BandaCache<Object, Object> cache;
	protected final CLogger logger;
	protected final String PURCHASE_ORDER = "Purchase Order";
	private T modelInstance;

	public BaseRepository() {
		Class<?> childClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		cache = GraphQLEndpoint.getCache(childClass);
		logger = CLogger.getCLogger(childClass);
	}

	public T getModelInstance() {
		if (modelInstance == null) {
			modelInstance = createModelInstance();
		}
		return modelInstance;
	}

	protected abstract T createModelInstance();

	/**
	 * This method can be overridden, but it maps entities, saves them, updates the cache, and returns the updated DB
	 * entity.
	 *
	 * @param inputEntity The input model to save.
	 * @return The updated DB model.
	 */
	public T save(S inputEntity) {
		T updatedEntity = mapInputModelToModel(inputEntity);
		updatedEntity.saveEx();
		updatedEntity = getByUuid((String) updatedEntity.get_Value(updatedEntity.getUUIDColumnName()));
		afterSave(inputEntity, updatedEntity);
		updateCacheAfterSave(updatedEntity);
		return updatedEntity;
	}

	/**
	 * Map the input entity to the DB entity. Usually for field mapping.
	 *
	 * @param entity The input entity to eventually be saved.
	 * @return A mapped DB entity ready to be saved
	 */
	public abstract T mapInputModelToModel(S entity);

	/**
	 * Handle any after-save logic, such as saving dependent entities.
	 *
	 * @param inputEntity The original entity that was passed in to be saved.
	 * @param entity      The saved DB entity.
	 * @return The updated entity, if any updates were needed.
	 */
	public T afterSave(S inputEntity, T entity) {
		return null;
	}

	/**
	 * This method handles dealing with items stored in the cache for this entity. For the getById methods, the entity
	 * is stored by the UUID. For the resolver methods, the entity is stored by it's DB ID. So, they both need to be
	 * cleared and updated.
	 *
	 * @param entity The updated and saved entity to the DB.
	 */
	public void updateCacheAfterSave(T entity) {
		// Just remove the entities from the cache to let them be reloaded later. This is helpful for when certain entities
		// are processed
		cache.delete(entity.get_ID());
		cache.delete(entity.get_Value(entity.getUUIDColumnName()));
		// TODO: Use the below in the future, if it's useful
//		// Only update things if they exist already so the cache doesn't become too full with unnecessary objects
//		if (cache.containsKey(entity.get_ID())) {
//			// The data loader likes entities stored as CompletableFutures, so store them that way
//			cache.set(entity.get_ID(), CompletableFuture.supplyAsync(() -> entity));
//		}
//		Object uuid = entity.get_Value(entity.getUUIDColumnName());
//		if (cache.containsKey(uuid)) {
//			// The data loader likes entities stored as CompletableFutures, so store them that way
//			cache.set(uuid, CompletableFuture.supplyAsync(() -> entity));
//		}
	}

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
	 * This should be overridden in inheriting classes.
	 *
	 * @return A WHERE clause to apply in all queries
	 */
	public String getDefaultWhereClause() {
		return "";
	}

	/**
	 * This should be overridden in inheriting classes.
	 *
	 * @return A parameters to use in all queries
	 */
	public List<Object> getDefaultWhereClauseParameters() {
		return new ArrayList<>();
	}

	/**
	 * This should be overridden in inheriting classes.
	 *
	 * @return A parameters to use in all queries
	 */
	public List<Object> getDefaultJoinClauseParameters() {
		return new ArrayList<>();
	}

	/**
	 * This should be overridden in inheriting classes.
	 *
	 * @return A JOIN clause to apply in all queries
	 */
	public String getDefaultJoinClause() {
		return "";
	}

	/**
	 * The default method to create a BandaQuery for this entity type. This handles adding the default WHERE and JOIN
	 * clauses
	 *
	 * @param additionalWhereClause An additional WHERE clause above the default
	 * @param parameters            Any parameters needed for the additional WHERE clause
	 * @return A query that can be used to fetch data
	 */
	public BandaQuery<T> getBaseQuery(String additionalWhereClause, Object... parameters) {
		String whereClause = additionalWhereClause;
		// If there's a default where clause, we need to add it (add to the end for parameter sequencing purposes)
		if (!getDefaultWhereClause().isEmpty()) {
			if (StringUtil.isNullOrEmpty(additionalWhereClause)) {
				whereClause = "";
			} else {
				whereClause = " AND " + whereClause;
			}
			// To ensure the parameters are added correctly, we'll assume passed-in parameters are added last
			whereClause = getDefaultWhereClause() + whereClause;
		}
		// Set up the query
		Query query = new Query(Env.getCtx(), getModelInstance().get_TableName(), whereClause, null)
				.setNoVirtualColumn(true);
		// If we should use the client ID in the context, add it
		if (shouldUseContextClientId()) {
			query.setClient_ID();
		}
		List<Object> parametersToUse = new ArrayList<>();
		// Add parameters in the specified order of query construction: JOIN, WHERE, additional WHERE
		// Add any JOIN clauses specified by default
		if (!getDefaultJoinClause().isEmpty()) {
			query.addJoinClause(getDefaultJoinClause());
			if (getDefaultJoinClauseParameters() != null) {
				parametersToUse.addAll(getDefaultJoinClauseParameters());
			}
		}
		// Handle any default parameters
		if (getDefaultWhereClauseParameters() != null) {
			parametersToUse.addAll(getDefaultWhereClauseParameters());
		}
		// Lastly, handle any that were passed in
		if (parameters != null) {
			Arrays.stream(parameters).forEach(parameter -> {
				if (parameter instanceof List<?>) {
					parametersToUse.addAll((List<?>) parameter);
				} else {
					parametersToUse.add(parameter);
				}
			});
		}
		if (!parametersToUse.isEmpty()) {
			query.setParameters(parametersToUse);
		}
		return new BandaQuery<T>() {
			@Override
			public List<T> list() throws DBException {
				return query.list();
			}

			@Override
			public T first() throws DBException {
				return query.first();
			}

			@Override
			public String getSQL() throws DBException {
				return query.getSQL();
			}

			@Override
			public BandaQuery<T> addJoinClause(String joinClause) {
				query.addJoinClause(joinClause);
				return this;
			}

			@Override
			public BandaQuery<T> setOrderBy(String orderBy) {
				query.setOrderBy(orderBy);
				return this;
			}

			@Override
			public int count() throws DBException {
				return query.count();
			}

			@Override
			public BandaQuery<T> setPage(int pPageSize, int pPagesToSkip) {
				query.setPage(pPageSize, pPagesToSkip);
				return this;
			}

			@Override
			public BandaQuery<T> setOnlyActiveRecords(boolean onlyActiveRecords) {
				query.setOnlyActiveRecords(onlyActiveRecords);
				return this;
			}

			@Override
			public BandaQuery<T> setParameters(List<Object> parameters) {
				query.setParameters(parameters);
				return this;
			}

			@Override
			public boolean match() throws DBException {
				return query.match();
			}
		};
	}

	/**
	 * Get a list of this entity grouped by IDs. This needs a string as it's ID (generated by Model.getModelKey)
	 * because there could be collisions between the table IDs could match the grouping IDs (which are typically
	 * foreign keys).
	 *
	 * @param groupingFunction The grouping function to apply for these entities
	 * @param columnToSearch   The search column to check in
	 * @param ids              The IDs to search by
	 * @return
	 */
	public CompletableFuture<Map<String, List<T>>> getGroupsByIdsCompletableFuture(
			Function<T, Integer> groupingFunction, String columnToSearch, Set<String> ids) {
		String modelName = ModelUtil.getModelFromKey(ids.iterator().next());
		return CompletableFuture.supplyAsync(() -> getGroupsByIds(
				groupingFunction, columnToSearch, ids.stream().map(ModelUtil::getPropertyFromKey).collect(Collectors.toSet())
		).entrySet().stream().collect(
				Collectors.toMap(entrySet -> ModelUtil.getModelKey(modelName, entrySet.getKey()), Map.Entry::getValue))
		);
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
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		if (!QueryUtil.doesTableAliasExistOnColumn(columnToSearch)) {
			columnToSearch = getModelInstance().get_TableName() + "." + columnToSearch;
		}
		BandaQuery<T> query = getBaseQuery(columnToSearch + " IN (" + whereCondition + ")", parameters)
				.setOnlyActiveRecords(true);
		List<T> models = query.list();
		return models.stream().collect(Collectors.groupingBy(groupingFunction));
	}

	public T getById(int id) {
		return getBaseQuery(getModelInstance().get_TableName() + "." +
				getModelInstance().get_TableName() + "_ID=?", id).first();
	}

	/**
	 * Get a list of entities by their IDs
	 *
	 * @param ids The IDs to search by
	 * @return
	 */
	public Map<Integer, T> getByIds(Set<Integer> ids) {
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		List<T> models = getBaseQuery(getModelInstance().get_TableName() + "." +
				getModelInstance().get_TableName() + "_ID IN (" + whereCondition + ")", parameters).list();
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
		return getBaseQuery(getModelInstance().getUUIDColumnName() + "=?", uuid).first();
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuids The UUIDs to search by
	 * @return
	 */
	public List<T> getByUuids(List<String> uuids) {
		String whereClause = uuids.stream().filter(uuid -> !StringUtil.isNullOrEmpty(uuid)).map(uuid -> "'" + uuid + "'")
				.collect(Collectors.joining(","));
		return getBaseQuery(getModelInstance().getUUIDColumnName() + " IN (" + whereClause + ")").list();
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuids The UUIDs to search by
	 * @return
	 */
	public CompletableFuture<Map<String, T>> getByUuidsCompletableFuture(Set<String> uuids) {
		return CompletableFuture.supplyAsync(() -> getByUuids(new ArrayList<>(uuids)).stream().collect(
				Collectors.toMap(entity -> entity.get_Value(entity.getUUIDColumnName()).toString(), entity -> entity))
		);
	}

	public Connection<T> get(String filterJson, String sort, PagingInfo pagingInfo, DataFetchingEnvironment environment) {
		return this.get(filterJson, sort, pagingInfo, null, null, null, environment);
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

			String filterWhereClause = FilterUtil.getWhereClauseFromFilter(getModelInstance(), filterJson, parameters);
			if (StringUtil.isNullOrEmpty(whereClause)) {
				whereClause = filterWhereClause;
			} else {
				whereClause += " AND " + filterWhereClause;
			}

			BandaQuery<T> query = getBaseQuery(whereClause, parameters);

			StringBuilder dynamicJoinClause = new StringBuilder();
			if (!getDynamicJoins().isEmpty()) {
				String currentJoinClause = (joinClause == null ? "" : joinClause).toLowerCase();
				// If the current query already has a JOIN (the default), we need to get it
				String currentQuerySql = query.getSQL();
				if (currentQuerySql.contains("JOIN")) {
					int firstJoinStatementIndex = currentQuerySql.indexOf("JOIN");
					int endOfJoinStatementIndex = currentQuerySql.contains("WHERE") ? currentQuerySql.indexOf("WHERE") :
							currentQuerySql.length();
					currentJoinClause += " " + currentQuerySql.substring(firstJoinStatementIndex, endOfJoinStatementIndex - 1);
				}

				// Now figure out which tables are needed based on the filter/sort criteria
				List<String> neededJoinTables = FilterUtil.getTablesNeedingJoins(filterJson);
				neededJoinTables.addAll(SortUtil.getTablesNeedingJoins(sortJson));
				neededJoinTables = neededJoinTables.stream().distinct().collect(Collectors.toList());
				for (String tableNeedingJoin : neededJoinTables) {
					// If this table was already specified in a JOIN, we don't need to dynamically add it
					if (currentJoinClause.contains(tableNeedingJoin + ".")) {
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

			String orderBy = SortUtil.getOrderByClauseFromSort(getModelInstance(), sortJson);
			if (orderBy != null) {
				query = query.setOrderBy(orderBy);
			}

			if (QueryUtil.isTotalCountRequested(environment)) {
				// get total count without pagination parameters
				pagingInfo.setTotalCount(query.count());
			}

			List<T> results = new ArrayList<>();
			if (QueryUtil.areResultsRequested(environment)) {
				// set pagination params
				query = query.setPage(pagingInfo.getPageSize(), pagingInfo.getPage());
				results = query.list();
			}
			return new Connection<>(results, pagingInfo);
		} catch (Exception ex) {
			throw new AdempiereException(ex);
		}
	}
}
