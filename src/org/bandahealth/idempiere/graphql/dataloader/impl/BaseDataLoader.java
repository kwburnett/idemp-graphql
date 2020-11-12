package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.repository.BaseRepository;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.PO;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.lang.reflect.ParameterizedType;
import java.util.Properties;

/**
 * The base data loader that holds logic common to all data loaders.
 *
 * @param <T> The iDempiere entity this data loader loads data for
 * @param <S> The input entity, if one exists, that the repository uses for receiving data
 * @param <R> The repository used to fetch data from the DB for this entity
 */
public abstract class BaseDataLoader<T extends PO, S extends T, R extends BaseRepository<T, S>> {
	/**
	 * A method to return the data loader name of the ID batch loader (these names must be unique)
	 *
	 * @return The name to register the ID data loader under
	 */
	protected abstract String getByIdDataLoaderName();

	/**
	 * A method to return the data loader name of the UUID batch loader (these names must be unique)
	 *
	 * @return The name to register the UUID data loader under
	 */
	protected abstract String getByUuidDataLoaderName();

	/**
	 * A method to return the repository instance for default data loader registration
	 *
	 * @return The repository to use for this class
	 */
	protected abstract R getRepositoryInstance();

	/**
	 * The base method to register a data loader by iDempiere model ID and UUID.
	 *
	 * @param registry         The data loader registry for this GraphQL query
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 */
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		if (!StringUtil.isNullOrEmpty(getByIdDataLoaderName())) {
			registry.register(getByIdDataLoaderName(), DataLoader.newMappedDataLoader(getByIdBatchLoader(),
					getOptionsWithCache(idempiereContext)));
		}
		if (!StringUtil.isNullOrEmpty(getByUuidDataLoaderName())) {
			registry.register(getByUuidDataLoaderName(), DataLoader.newMappedDataLoader(getByUuidBatchLoader(),
					getOptionsWithCache(idempiereContext)));
		}
	}

	/**
	 * This creates a cache specific to the entity by leveraging the subclass's type.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A DataLoaderOptions containing a cache specific to the iDempiere entity T
	 */
	protected DataLoaderOptions getOptionsWithCache(Properties idempiereContext) {
		return DataLoaderOptions.newOptions().setCacheMap(GraphQLEndpoint.getCache(
				((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
		)).setBatchLoaderContextProvider(() -> idempiereContext);
	}

	/**
	 * This creates a cache specific to the entity by leveraging the subclass's type.
	 *
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return A DataLoaderOptions containing a cache specific to the iDempiere entity T
	 */
	protected DataLoaderOptions getOptionsWithoutCache(Properties idempiereContext) {
		return DataLoaderOptions.newOptions().setBatchLoaderContextProvider(() -> idempiereContext);
	}

	/**
	 * The method to return batched data by DB ID
	 *
	 * @return A batch loader for loading entities by their DB IDs
	 */
	private MappedBatchLoaderWithContext<Integer, T> getByIdBatchLoader() {
		return (keys, batchLoaderEnvironment) -> getRepositoryInstance()
				.getByIdsCompletableFuture(keys, batchLoaderEnvironment.getContext());
	}

	/**
	 * The method to return batched data by UUID
	 *
	 * @return A batch loader for loading entities by UUIDs
	 */
	private MappedBatchLoaderWithContext<String, T> getByUuidBatchLoader() {
		return (keys, batchLoaderEnvironment) -> getRepositoryInstance()
				.getByUuidsCompletableFuture(keys, batchLoaderEnvironment.getContext());
	}
}
