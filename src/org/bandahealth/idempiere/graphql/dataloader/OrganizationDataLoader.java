package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.OrganizationRepository;
import org.compiere.model.MOrg;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class OrganizationDataLoader extends BaseDataLoader<MOrg, MOrg, OrganizationRepository>
		implements DataLoaderRegisterer {

	public static String ORGANIZATION_DATA_LOADER = "organizationDataLoader";
	public static String ORGANIZATION_BY_CLIENT_DATA_LOADER = "organizationByClientDataLoader";
	private final OrganizationRepository organizationRepository;

	public OrganizationDataLoader() {
		organizationRepository = new OrganizationRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ORGANIZATION_DATA_LOADER;
	}

	@Override
	protected OrganizationRepository getRepositoryInstance() {
		return organizationRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(ORGANIZATION_BY_CLIENT_DATA_LOADER,
				DataLoader.newMappedDataLoader(getOrganizationByClientBatchLoader(), getOptionsWithCache()));
	}

	private MappedBatchLoader<Integer, List<MOrg>> getOrganizationByClientBatchLoader() {
		return keys -> organizationRepository.getGroupsByIds(MOrg::getAD_Client_ID, MOrg.COLUMNNAME_AD_Client_ID, keys);
	}
}
