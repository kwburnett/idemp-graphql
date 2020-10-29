package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.model.MRole;

public class RoleDataLoader extends BaseDataLoader<MRole, MRole, RoleRepository> implements DataLoaderRegisterer {

	public static final String ROLE_DATA_LOADER = "roleDataLoader";
	private final RoleRepository roleRepository;

	public RoleDataLoader() {
		roleRepository = new RoleRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ROLE_DATA_LOADER;
	}

	@Override
	protected RoleRepository getRepositoryInstance() {
		return roleRepository;
	}
}
