package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.model.MRole;

public class RoleDataLoader extends BaseDataLoader<MRole, RoleRepository> implements DataLoaderRegisterer {

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
