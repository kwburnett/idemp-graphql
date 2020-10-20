package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.WarehouseRepository;
import org.compiere.model.MWarehouse;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class WarehouseDataLoader extends BaseDataLoader<MWarehouse, WarehouseRepository>
		implements DataLoaderRegisterer {

	public static final String WAREHOUSE_DATA_LOADER = "warehouseDataLoader";
	public static final String WAREHOUSE_BY_ORGANIZATION_DATA_LOADER = "warehouseByOrganizationDataLoader";
	private final WarehouseRepository warehouseRepository;

	public WarehouseDataLoader() {
		warehouseRepository = new WarehouseRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return WAREHOUSE_DATA_LOADER;
	}

	@Override
	protected WarehouseRepository getRepositoryInstance() {
		return warehouseRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(WAREHOUSE_BY_ORGANIZATION_DATA_LOADER,
				DataLoader.newMappedDataLoader(getByOrganizationBatchLoader()));
	}

	private MappedBatchLoader<Integer, List<MWarehouse>> getByOrganizationBatchLoader() {
		return keys -> warehouseRepository.getGroupsByIds(MWarehouse::getAD_Org_ID, MWarehouse.COLUMNNAME_AD_Org_ID, keys);
	}
}
