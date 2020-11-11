package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.OrganizationRepository;
import org.compiere.model.MOrg;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class OrganizationDataLoader extends BaseDataLoader<MOrg, MOrg, OrganizationRepository>
		implements DataLoaderRegisterer {
	public static String ORGANIZATION_BY_ID_DATA_LOADER = "organizationByIdDataLoader";
	public static String ORGANIZATION_BY_UUID_DATA_LOADER = "organizationByUuidDataLoader";
	public static String ORGANIZATION_BY_CLIENT_DATA_LOADER = "organizationByClientDataLoader";
	private final OrganizationRepository organizationRepository;

	public OrganizationDataLoader() {
		organizationRepository = new OrganizationRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ORGANIZATION_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ORGANIZATION_BY_UUID_DATA_LOADER;
	}

	@Override
	protected OrganizationRepository getRepositoryInstance() {
		return organizationRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(ORGANIZATION_BY_CLIENT_DATA_LOADER,
				DataLoader.newMappedDataLoader(getOrganizationByClientBatchLoader(), getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MOrg>> getOrganizationByClientBatchLoader() {
		return (keys, batchLoaderEnvironment) -> organizationRepository.getGroupsByIdsCompletableFuture(
				MOrg::getAD_Client_ID, MOrg.COLUMNNAME_AD_Client_ID, keys, batchLoaderEnvironment.getContext());
	}
}
