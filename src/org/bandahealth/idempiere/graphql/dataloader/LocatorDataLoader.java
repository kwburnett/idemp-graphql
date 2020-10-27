package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.LocatorRepository;
import org.compiere.model.MLocator;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class LocatorDataLoader extends BaseDataLoader<MLocator, MLocator, LocatorRepository>
		implements DataLoaderRegisterer {

	public static final String LOCATOR_DATA_LOADER = "locatorDataLoader";
	public static final String LOCATOR_BY_WAREHOUSE_DATA_LOADER = "locatorByWarehouseDataLoader";
	private final LocatorRepository locatorRepository;

	public LocatorDataLoader() {
		locatorRepository = new LocatorRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(LOCATOR_BY_WAREHOUSE_DATA_LOADER, DataLoader.newMappedDataLoader(getByWarehouseBatchLoader(),
				getOptionsWithCache()));
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return LOCATOR_DATA_LOADER;
	}

	@Override
	protected LocatorRepository getRepositoryInstance() {
		return locatorRepository;
	}

	private MappedBatchLoader<Integer, List<MLocator>> getByWarehouseBatchLoader() {
		return keys -> locatorRepository.getGroupsByIdsCompletableFuture(MLocator::getM_Warehouse_ID,
				MLocator.COLUMNNAME_M_Warehouse_ID, keys);
	}
}
