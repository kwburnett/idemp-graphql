package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.BaseRepository;
import org.compiere.model.PO;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public abstract class BaseDataLoader<T extends PO, S extends BaseRepository<T>> {

	protected abstract String getDefaultDataLoaderName();
	protected abstract S getRepositoryInstance();

	public void register(DataLoaderRegistry registry) {
		registry.register(getDefaultDataLoaderName(), DataLoader.newMappedDataLoader(getBatchLoader()));
	}

	private MappedBatchLoader<Integer, T> getBatchLoader() {
		return getRepositoryInstance()::getByIds;
	}
}
