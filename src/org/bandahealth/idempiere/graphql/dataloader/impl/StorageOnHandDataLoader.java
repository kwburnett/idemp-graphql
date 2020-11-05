package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.StorageOnHandInput;
import org.bandahealth.idempiere.graphql.repository.StorageOnHandRepository;
import org.compiere.model.MStorageOnHand;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class StorageOnHandDataLoader extends BaseDataLoader<MStorageOnHand, StorageOnHandInput, StorageOnHandRepository>
		implements DataLoaderRegisterer {
	public static final String STORAGE_ON_HAND_BY_ID_DATA_LOADER = "storageOnHandByIdDataLoader";
	public static final String STORAGE_ON_HAND_BY_UUID_DATA_LOADER = "storageOnHandByUuidDataLoader";
	public static final String STORAGE_ON_HAND_BY_PRODUCT_DATA_LOADER = "storageOnHandByProductDataLoader";
	private final StorageOnHandRepository storageOnHandRepository;

	public StorageOnHandDataLoader() {
		storageOnHandRepository = new StorageOnHandRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(STORAGE_ON_HAND_BY_PRODUCT_DATA_LOADER, DataLoader.newMappedDataLoader(getByProductBatchLoader(),
				getOptionsWithCache()));
	}

	@Override
	protected String getByIdDataLoaderName() {
		return STORAGE_ON_HAND_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return STORAGE_ON_HAND_BY_UUID_DATA_LOADER;
	}

	@Override
	protected StorageOnHandRepository getRepositoryInstance() {
		return storageOnHandRepository;
	}

	private MappedBatchLoader<String, List<MStorageOnHand>> getByProductBatchLoader() {
		return keys -> storageOnHandRepository.getGroupsByIdsCompletableFuture(MStorageOnHand::getM_Product_ID,
				MStorageOnHand.COLUMNNAME_M_Product_ID, keys);
	}
}
