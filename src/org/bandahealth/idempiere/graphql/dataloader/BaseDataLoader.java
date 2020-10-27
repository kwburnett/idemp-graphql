package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.GraphQLEndpoint;
import org.bandahealth.idempiere.graphql.repository.BaseRepository;
import org.compiere.model.PO;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.lang.reflect.ParameterizedType;

public abstract class BaseDataLoader<T extends PO, S extends T, R extends BaseRepository<T, S>> {

	protected abstract String getDefaultDataLoaderName();

	protected abstract R getRepositoryInstance();

	public void register(DataLoaderRegistry registry) {
		registry.register(getDefaultDataLoaderName(), DataLoader.newMappedDataLoader(getBatchLoader(),
				getOptionsWithCache()));
	}

	protected DataLoaderOptions getOptionsWithCache() {
		return DataLoaderOptions.newOptions().setCacheMap(GraphQLEndpoint.getCache(
				((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
		));
	}

	private MappedBatchLoader<Integer, T> getBatchLoader() {
		return getRepositoryInstance()::getByIdsCompletableFuture;
	}
}
