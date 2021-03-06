package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.cache.BandaCache;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.utils.BandaQuery;
import org.bandahealth.idempiere.graphql.utils.FilterUtil;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.bandahealth.idempiere.graphql.utils.SortUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
		Class<?> childClass =
				((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		cache = GraphQLEndpoint.getCache(childClass);
		logger = CLogger.getCLogger(childClass);
	}

	/**
	 * Get a model instance. If one does not exist, it is created. This should NOT be used to get something to save to
	 * the DB.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A model instance
	 */
	public T getModelInstance(Properties idempiereContext) {
		if (modelInstance == null) {
			modelInstance = createModelInstance(idempiereContext);
		}
		return modelInstance;
	}

	/**
	 * The method to create a new model instance. This should be used when getting something to save to the DB.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A model instance
	 */
	protected abstract T createModelInstance(Properties idempiereContext);

	/**
	 * This method can be overridden, but it maps entities, saves them, updates the cache, and returns the updated DB
	 * entity.
	 *
	 * @param inputEntity      The input model to save.
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return The updated DB model.
	 */
	public T save(S inputEntity, Properties idempiereContext) {
		T updatedEntity = mapInputModelToModel(inputEntity, idempiereContext);
		updatedEntity.saveEx();
		updatedEntity = getByUuid((String) updatedEntity.get_Value(updatedEntity.getUUIDColumnName()), idempiereContext);
		afterSave(inputEntity, updatedEntity, idempiereContext);
		updateCacheAfterSave(updatedEntity, idempiereContext);
		return updatedEntity;
	}

	/**
	 * Map the input entity to the DB entity. Usually for field mapping.
	 *
	 * @param entity           The input entity to eventually be saved.
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A mapped DB entity ready to be saved
	 */
	public abstract T mapInputModelToModel(S entity, Properties idempiereContext);

	/**
	 * Handle any after-save logic, such as saving dependent entities.
	 *
	 * @param inputEntity      The original entity that was passed in to be saved.
	 * @param entity           The saved DB entity.
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return The updated entity, if any updates were needed.
	 */
	public T afterSave(S inputEntity, T entity, Properties idempiereContext) {
		return null;
	}

	/**
	 * This method handles dealing with items stored in the cache for this entity. For the getById methods, the entity
	 * is stored by the UUID. For the resolver methods, the entity is stored by it's DB ID. So, they both need to be
	 * cleared and updated.
	 *
	 * @param entity The updated and saved entity to the DB.
	 */
	public void updateCacheAfterSave(T entity, Properties idempiereContext) {
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
	 * Whether the client ID for the context should be automatically used in any DB queries
	 *
	 * @return Whether the client ID from the iDempiere context will be used
	 */
	protected boolean shouldUseContextClientId() {
		return true;
	}

	/**
	 * Get any JOINs that should only be used if a sort/filter is passed in using them
	 * Structure: Map<TableName, JOIN clause>
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A map of table names and their appropriate JOIN clauses
	 */
	public Map<String, String> getDynamicJoins(Properties idempiereContext) {
		return new HashMap<>();
	}

	/**
	 * Get any default WHERE clauses that should be applied to every query.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A WHERE clause to apply in all queries
	 */
	public String getDefaultWhereClause(Properties idempiereContext) {
		return "";
	}

	/**
	 * Any parameters that are needed by the default WHERE clauses
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A parameters to use in all queries
	 */
	public List<Object> getDefaultWhereClauseParameters(Properties idempiereContext) {
		return new ArrayList<>();
	}

	/**
	 * Any parameters that are needed by the default JOIN clauses
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A parameters to use in all queries
	 */
	public List<Object> getDefaultJoinClauseParameters(Properties idempiereContext) {
		return new ArrayList<>();
	}

	/**
	 * Get any default JOIN clauses that should be applied to every query.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A JOIN clause to apply in all queries
	 */
	public String getDefaultJoinClause(Properties idempiereContext) {
		return "";
	}

	/**
	 * The default method to create a BandaQuery for this entity type. This handles adding the default WHERE and JOIN
	 * clauses
	 *
	 * @param idempiereContext      The context since Env.getCtx() isn't thread-safe
	 * @param additionalWhereClause An additional WHERE clause above the default
	 * @param parameters            Any parameters needed for the additional WHERE clause
	 * @return A query that can be used to fetch data
	 */
	public BandaQuery<T> getBaseQuery(Properties idempiereContext, String additionalWhereClause,
			Object... parameters) {
		String whereClause = additionalWhereClause;
		// If there's a default where clause, we need to add it (add to the end for parameter sequencing purposes)
		if (!getDefaultWhereClause(idempiereContext).isEmpty()) {
			if (StringUtil.isNullOrEmpty(additionalWhereClause)) {
				whereClause = "";
			} else {
				whereClause = " AND " + whereClause;
			}
			// To ensure the parameters are added correctly, we'll assume passed-in parameters are added last
			whereClause = getDefaultWhereClause(idempiereContext) + whereClause;
		}
		// Set up the query. Also, we don't want virtual columns because those were used in GO and greatly slow down
		// queries. If they're needed, the query should be written in the repositories as a column/JOIN
		Query query = new Query(idempiereContext, getModelInstance(idempiereContext).get_TableName(), whereClause,
				null).setNoVirtualColumn(true);
		// If we should use the client ID in the context, add it
		if (shouldUseContextClientId()) {
			query.setClient_ID();
		}
		List<Object> parametersToUse = new ArrayList<>();
		// Add parameters in the specified order of query construction: JOIN, WHERE, additional WHERE
		// Add any JOIN clauses specified by default
		if (!getDefaultJoinClause(idempiereContext).isEmpty()) {
			query.addJoinClause(getDefaultJoinClause(idempiereContext));
			if (getDefaultJoinClauseParameters(idempiereContext) != null) {
				parametersToUse.addAll(getDefaultJoinClauseParameters(idempiereContext));
			}
		}
		// Handle any default parameters
		if (getDefaultWhereClauseParameters(idempiereContext) != null) {
			parametersToUse.addAll(getDefaultWhereClauseParameters(idempiereContext));
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
		return new BandaQuery<>() {
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
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A completable future of entities grouped by an Entity Key
	 */
	public CompletableFuture<Map<String, List<T>>> getGroupsByIdsCompletableFuture(Function<T, Integer> groupingFunction,
			String columnToSearch, Set<String> ids, Properties idempiereContext) {
		String modelName = ModelUtil.getModelFromKey(ids.iterator().next());
		return CompletableFuture.supplyAsync(() -> getGroupsByIds(groupingFunction, columnToSearch,
				ids.stream().map(ModelUtil::getIdFromKey).collect(Collectors.toSet()), idempiereContext).entrySet()
				.stream().collect(
						Collectors.toMap(entrySet -> ModelUtil.getModelKey(modelName, entrySet.getKey()), Map.Entry::getValue)
				)
		);
	}

	/**
	 * Get a list of this entity grouped by IDs
	 *
	 * @param groupingFunction The grouping function to apply for these entities
	 * @param columnToSearch   The search column to check in
	 * @param ids              The IDs to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return Entities grouped by their ID
	 */
	public Map<Integer, List<T>> getGroupsByIds(Function<T, Integer> groupingFunction, String columnToSearch,
			Set<Integer> ids, Properties idempiereContext) {
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		if (!QueryUtil.doesTableAliasExistOnColumn(columnToSearch)) {
			columnToSearch = getModelInstance(idempiereContext).get_TableName() + "." + columnToSearch;
		}
		BandaQuery<T> query = getBaseQuery(idempiereContext, columnToSearch + " IN (" + whereCondition +
				")", parameters).setOnlyActiveRecords(true);
		List<T> models = query.list();
		return models.stream().collect(Collectors.groupingBy(groupingFunction));
	}

	/**
	 * Get an entity by its ID
	 *
	 * @param id               The ID to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return The entity
	 */
	public T getById(int id, Properties idempiereContext) {
		return getBaseQuery(idempiereContext, getModelInstance(idempiereContext).get_TableName() + "." +
				getModelInstance(idempiereContext).get_TableName() + "_ID=?", id).first();
	}

	/**
	 * Get a list of entities by their IDs
	 *
	 * @param ids              The IDs to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A map of entities by the ID searched
	 */
	public Map<Integer, T> getByIds(Set<Integer> ids, Properties idempiereContext) {
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		List<T> models = getBaseQuery(idempiereContext,
				getModelInstance(idempiereContext).get_TableName() + "." + getModelInstance(idempiereContext)
						.get_TableName() + "_ID IN (" + whereCondition + ")", parameters).list();
		return models.stream().collect(Collectors.toMap(T::get_ID, m -> m));
	}

	/**
	 * Get a list of entities by their IDs, but as a completable future
	 *
	 * @param ids              The IDs to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A completable future of a map of entities by their IDs
	 */
	public CompletableFuture<Map<Integer, T>> getByIdsCompletableFuture(Set<Integer> ids, Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getByIds(ids, idempiereContext));
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuid             The UUID to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return An entity
	 */
	public T getByUuid(String uuid, Properties idempiereContext) {
		return getBaseQuery(idempiereContext, getModelInstance(idempiereContext).getUUIDColumnName() +
				"=?", uuid).first();
	}

	/**
	 * Get an of entity by it's UUID
	 *
	 * @param uuids            The UUIDs to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A list of entities
	 */
	public List<T> getByUuids(List<String> uuids, Properties idempiereContext) {
		String whereClause = uuids.stream().filter(uuid -> !StringUtil.isNullOrEmpty(uuid)).map(uuid -> "'" + uuid + "'")
				.collect(Collectors.joining(","));
		return getBaseQuery(idempiereContext, getModelInstance(idempiereContext).getUUIDColumnName() +
				" IN (" + whereClause + ")").list();
	}

	/**
	 * Get an of entity by its UUID, but as a completable future
	 *
	 * @param uuids            The UUIDs to search by
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A completable future of a map of entities by their UUIDs
	 */
	public CompletableFuture<Map<String, T>> getByUuidsCompletableFuture(Set<String> uuids,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getByUuids(new ArrayList<>(uuids), idempiereContext).stream().collect(
				Collectors.toMap(entity -> entity.get_Value(entity.getUUIDColumnName()).toString(), entity -> entity))
		);
	}

	/**
	 * Get a list of entities from the DB
	 *
	 * @param filterJson  A filter to apply to the query
	 * @param sort        How the data should be sorted
	 * @param pagingInfo  What page of data is needed
	 * @param environment The environment associated with all calls, containing context, which is used for data
	 *                    look-aheads to allow the DB queries to be smart and only be run if they're needed
	 * @return A list of entities
	 */
	public Connection<T> get(String filterJson, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		return this.get(filterJson, sort, pagingInfo, null, null, null, environment);
	}

	/**
	 * Get a list of entities from the DB
	 *
	 * @param filterJson  A filter to apply to the query
	 * @param sort        How the data should be sorted
	 * @param pagingInfo  What page of data is needed
	 * @param whereClause A WHERE clause that should be added to the query
	 * @param environment The environment associated with all calls, containing context, which is used for data
	 *                    look-aheads to allow the DB queries to be smart and only be run if they're needed
	 * @return A list of entities
	 */
	public Connection<T> get(String filterJson, String sort, PagingInfo pagingInfo, String whereClause,
			List<Object> parameters, DataFetchingEnvironment environment) {
		return this.get(filterJson, sort, pagingInfo, whereClause, parameters, null, environment);
	}

	/**
	 * Get all with the inclusion of a join clause for joined cases of sorting
	 *
	 * @param filterJson  A filter to apply to the query
	 * @param sortJson    How the data should be sorted
	 * @param pagingInfo  What page of data is needed
	 * @param whereClause A WHERE clause that should be added to the query
	 * @param parameters  Any parameters to use in the query
	 * @param joinClause  Use to specify a linked table so joining can occur
	 * @return A list of entities
	 */
	public Connection<T> get(String filterJson, String sortJson, PagingInfo pagingInfo, String whereClause,
			List<Object> parameters, String joinClause, DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		try {
			if (parameters == null) {
				parameters = new ArrayList<>();
			}

			String filterWhereClause = FilterUtil.getWhereClauseFromFilter(getModelInstance(idempiereContext), filterJson,
					parameters);
			if (StringUtil.isNullOrEmpty(whereClause)) {
				whereClause = filterWhereClause;
			} else {
				whereClause += " AND " + filterWhereClause;
			}

			BandaQuery<T> query = getBaseQuery(idempiereContext, whereClause, parameters);

			StringBuilder dynamicJoinClause = new StringBuilder();
			if (!getDynamicJoins(idempiereContext).isEmpty()) {
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
					for (String dynamicTableJoinName : getDynamicJoins(idempiereContext).keySet()) {
						if (dynamicTableJoinName.equalsIgnoreCase(tableNeedingJoin)) {
							dynamicJoinClause.append(" ").append(getDynamicJoins(idempiereContext).get(dynamicTableJoinName));
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

			String orderBy = SortUtil.getOrderByClauseFromSort(getModelInstance(idempiereContext), sortJson);
			if (orderBy != null) {
				query = query.setOrderBy(orderBy);
			}

			// If the paging info wasn't requested in the payload, don't do an extra DB call to get it
			if (QueryUtil.isTotalCountRequested(environment)) {
				// get total count without pagination parameters
				pagingInfo.setTotalCount(query.count());
			}

			List<T> results = new ArrayList<>();
			// If the results weren't requested in the payload (say a consumer just wants to know the count of entities
			// matching a specified filter), don't do an extra DB call to get them
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
