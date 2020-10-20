package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.respository.RoleRepository;
import org.compiere.model.MRole;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

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
