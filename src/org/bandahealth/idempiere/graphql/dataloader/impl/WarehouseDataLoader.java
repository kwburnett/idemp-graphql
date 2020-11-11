package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.WarehouseRepository;
import org.compiere.model.MWarehouse;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class WarehouseDataLoader extends BaseDataLoader<MWarehouse, MWarehouse, WarehouseRepository>
		implements DataLoaderRegisterer {
	public static final String WAREHOUSE_BY_ID_DATA_LOADER = "warehouseByIdDataLoader";
	public static final String WAREHOUSE_BY_UUID_DATA_LOADER = "warehouseByUuidDataLoader";
	public static final String WAREHOUSE_BY_ORGANIZATION_DATA_LOADER = "warehouseByOrganizationDataLoader";
	private final WarehouseRepository warehouseRepository;

	public WarehouseDataLoader() {
		warehouseRepository = new WarehouseRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return WAREHOUSE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return WAREHOUSE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected WarehouseRepository getRepositoryInstance() {
		return warehouseRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(WAREHOUSE_BY_ORGANIZATION_DATA_LOADER,
				DataLoader.newMappedDataLoader(getByOrganizationBatchLoader(), getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MWarehouse>> getByOrganizationBatchLoader() {
		return (keys, batchLoaderEnvironment) -> warehouseRepository.getGroupsByIdsCompletableFuture(
				MWarehouse::getAD_Org_ID, MWarehouse.COLUMNNAME_AD_Org_ID, keys, batchLoaderEnvironment.getContext());
	}
}
