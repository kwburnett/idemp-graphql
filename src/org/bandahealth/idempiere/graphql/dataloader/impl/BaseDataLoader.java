package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.repository.BaseRepository;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.PO;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.lang.reflect.ParameterizedType;

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
	 * @param registry The data loader registry for this GraphQL query
	 */
	public void register(DataLoaderRegistry registry) {
		if (!StringUtil.isNullOrEmpty(getByIdDataLoaderName())) {
			registry.register(getByIdDataLoaderName(), DataLoader.newMappedDataLoader(getByIdBatchLoader(),
					getOptionsWithCache()));
		}
		if (!StringUtil.isNullOrEmpty(getByUuidDataLoaderName())) {
			registry.register(getByUuidDataLoaderName(), DataLoader.newMappedDataLoader(getByUuidBatchLoader(),
					getOptionsWithCache()));
		}
	}

	/**
	 * This creates a cache specific to the entity by leveraging the subclass's type.
	 *
	 * @return A DataLoaderOptions containing a cache specific to the iDempiere entity T
	 */
	protected DataLoaderOptions getOptionsWithCache() {
		return DataLoaderOptions.newOptions().setCacheMap(GraphQLEndpoint.getCache(
				((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
		));
	}

	/**
	 * The method to return batched data by DB ID
	 *
	 * @return A batch loader for loading entities by their DB IDs
	 */
	private MappedBatchLoader<Integer, T> getByIdBatchLoader() {
		return getRepositoryInstance()::getByIdsCompletableFuture;
	}

	/**
	 * The method to return batched data by UUID
	 *
	 * @return A batch loader for loading entities by UUIDs
	 */
	private MappedBatchLoader<String, T> getByUuidBatchLoader() {
		return getRepositoryInstance()::getByUuidsCompletableFuture;
	}
}
