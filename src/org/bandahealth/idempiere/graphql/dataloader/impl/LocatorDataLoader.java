package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.LocatorRepository;
import org.compiere.model.MLocator;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class LocatorDataLoader extends BaseDataLoader<MLocator, MLocator, LocatorRepository>
		implements DataLoaderRegisterer {
	public static final String LOCATOR_BY_ID_DATA_LOADER = "locatorByIdDataLoader";
	public static final String LOCATOR_BY_UUID_DATA_LOADER = "locatorByUuidDataLoader";
	public static final String LOCATOR_BY_WAREHOUSE_DATA_LOADER = "locatorByWarehouseDataLoader";
	private final LocatorRepository locatorRepository;

	public LocatorDataLoader() {
		locatorRepository = new LocatorRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(LOCATOR_BY_WAREHOUSE_DATA_LOADER, DataLoader.newMappedDataLoader(getByWarehouseBatchLoader(),
				getOptionsWithCache(idempiereContext)));
	}

	@Override
	protected String getByIdDataLoaderName() {
		return LOCATOR_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return LOCATOR_BY_UUID_DATA_LOADER;
	}

	@Override
	protected LocatorRepository getRepositoryInstance() {
		return locatorRepository;
	}

	private MappedBatchLoaderWithContext<String, List<MLocator>> getByWarehouseBatchLoader() {
		return (keys, batchLoaderEnvironment) -> locatorRepository
				.getGroupsByIdsCompletableFuture(MLocator::getM_Warehouse_ID, MLocator.COLUMNNAME_M_Warehouse_ID, keys,
						batchLoaderEnvironment.getContext());
	}
}
