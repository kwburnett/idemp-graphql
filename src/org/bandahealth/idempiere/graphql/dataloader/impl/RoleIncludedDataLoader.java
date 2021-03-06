package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.RoleIncludedRepository;
import org.compiere.model.MRoleIncluded;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class RoleIncludedDataLoader extends BaseDataLoader<MRoleIncluded, MRoleIncluded, RoleIncludedRepository>
		implements DataLoaderRegisterer {
	public static final String ROLE_INCLUDED_BY_ID_DATA_LOADER = "roleIncludedByIdDataLoader";
	public static final String ROLE_INCLUDED_BY_UUID_DATA_LOADER = "roleIncludedByUuidDataLoader";
	public static final String ROLE_INCLUDED_BY_ROLE_DATA_LOADER = "roleIncludedByRoleDataLoader";
	private final RoleIncludedRepository roleIncludedRepository;

	public RoleIncludedDataLoader() {
		roleIncludedRepository = new RoleIncludedRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ROLE_INCLUDED_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ROLE_INCLUDED_BY_UUID_DATA_LOADER;
	}

	@Override
	protected RoleIncludedRepository getRepositoryInstance() {
		return roleIncludedRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(ROLE_INCLUDED_BY_ROLE_DATA_LOADER, DataLoader.newMappedDataLoader(getByRoleBatchLoader(),
				getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MRoleIncluded>> getByRoleBatchLoader() {
		return (keys, batchLoaderEnvironment) -> roleIncludedRepository.getGroupsByIdsCompletableFuture(
				MRoleIncluded::getAD_Role_ID, MRoleIncluded.COLUMNNAME_AD_Role_ID, keys, batchLoaderEnvironment.getContext());
	}
}
