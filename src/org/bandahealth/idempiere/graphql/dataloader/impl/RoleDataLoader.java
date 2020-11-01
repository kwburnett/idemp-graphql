package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.model.MRole;

public class RoleDataLoader extends BaseDataLoader<MRole, MRole, RoleRepository> implements DataLoaderRegisterer {
	public static final String ROLE_BY_ID_DATA_LOADER = "roleByIdDataLoader";
	public static final String ROLE_BY_UUID_DATA_LOADER = "roleByUuidDataLoader";
	private final RoleRepository roleRepository;

	public RoleDataLoader() {
		roleRepository = new RoleRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ROLE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ROLE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected RoleRepository getRepositoryInstance() {
		return roleRepository;
	}
}
