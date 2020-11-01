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

public abstract class BaseDataLoader<T extends PO, S extends T, R extends BaseRepository<T, S>> {

	protected abstract String getDefaultByIdDataLoaderName();

	protected abstract String getDefaultByUuidDataLoaderName();

	protected abstract R getRepositoryInstance();

	public void register(DataLoaderRegistry registry) {
		if (!StringUtil.isNullOrEmpty(getDefaultByIdDataLoaderName())) {
			registry.register(getDefaultByIdDataLoaderName(), DataLoader.newMappedDataLoader(getByIdBatchLoader(),
					getOptionsWithCache()));
		}
		if (!StringUtil.isNullOrEmpty(getDefaultByUuidDataLoaderName())) {
			registry.register(getDefaultByUuidDataLoaderName(), DataLoader.newMappedDataLoader(getByUuidBatchLoader(),
					getOptionsWithCache()));
		}
	}

	protected DataLoaderOptions getOptionsWithCache() {
		return DataLoaderOptions.newOptions().setCacheMap(GraphQLEndpoint.getCache(
				((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
		));
	}

	private MappedBatchLoader<Integer, T> getByIdBatchLoader() {
		return getRepositoryInstance()::getByIdsCompletableFuture;
	}

	private MappedBatchLoader<String, T> getByUuidBatchLoader() {
		return getRepositoryInstance()::getByUuidsCompletableFuture;
	}
}
