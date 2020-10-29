package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleIncludedDataLoader;
import org.bandahealth.idempiere.graphql.repository.HomeScreenButtonRepository;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.model.MRole;
import org.compiere.model.MRoleIncluded;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoleResolver extends BaseResolver<MRole> implements GraphQLResolver<MRole> {
	private final RoleRepository roleRepository;
	private final HomeScreenButtonRepository homeScreenButtonRepository;

	public RoleResolver() {
		roleRepository = new RoleRepository();
		homeScreenButtonRepository = new HomeScreenButtonRepository();
	}

	public CompletableFuture<List<MRole>> includedRoles(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.includedRoles(entity.getAD_Role_ID(), environment);
	}

	public CompletableFuture<Boolean> isAdmin(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.isAdmin(entity.getAD_Role_ID(), environment);
	}

	public CompletableFuture<Boolean> hasAccessToReports(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.isAdmin(entity.getAD_Role_ID(), environment)
				.thenApply(homeScreenButtonRepository::hasAccessToReports);
	}

	private CompletableFuture<List<MRoleIncluded>> rolesIncluded(MRole entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MRoleIncluded>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(RoleIncludedDataLoader.ROLE_INCLUDED_BY_ROLE_DATA_LOADER);
		return dataLoader.load(entity.getAD_Role_ID());
	}
}
