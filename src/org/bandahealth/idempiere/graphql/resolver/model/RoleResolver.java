package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.dataloader.impl.RoleIncludedDataLoader;
import org.bandahealth.idempiere.graphql.repository.DashboardButtonGroupButtonRepository;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MRole;
import org.compiere.model.MRoleIncluded;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Role resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class RoleResolver extends BaseResolver<MRole> implements GraphQLResolver<MRole> {
	private final RoleRepository roleRepository;
	private final DashboardButtonGroupButtonRepository dashboardButtonGroupButtonRepository;

	/**
	 * This resolver leverages the repositories instead of data loaders due to the unique nature of fetching data for
	 * roles
	 */
	public RoleResolver() {
		roleRepository = new RoleRepository();
		dashboardButtonGroupButtonRepository = new DashboardButtonGroupButtonRepository();
	}

	public CompletableFuture<List<MRole>> includedRoles(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.includedRoles(entity.getAD_Role_ID(), environment);
	}

	public CompletableFuture<Boolean> isAdmin(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.isAdmin(entity.getAD_Role_ID(), environment);
	}

	public CompletableFuture<Boolean> hasAccessToReports(MRole entity, DataFetchingEnvironment environment) {
		return roleRepository.isAdmin(entity.getAD_Role_ID(), environment)
				.thenApply((userIsAdmin) -> dashboardButtonGroupButtonRepository.hasAccessToReports(userIsAdmin,
						BandaGraphQLContext.getCtx(environment)));
	}

	private CompletableFuture<List<MRoleIncluded>> rolesIncluded(MRole entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MRoleIncluded>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(RoleIncludedDataLoader.ROLE_INCLUDED_BY_ROLE_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getAD_Role_ID()));
	}
}
