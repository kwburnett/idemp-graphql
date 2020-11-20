package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MHomeScreenButton;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.DashboardButtonGroupButtonRepository;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.util.Env;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class HomeScreenButtonQuery implements GraphQLQueryResolver {
	private final DashboardButtonGroupButtonRepository dashboardButtonGroupButtonRepository;
	private final RoleRepository roleRepository;

	public HomeScreenButtonQuery() {
		dashboardButtonGroupButtonRepository = new DashboardButtonGroupButtonRepository();
		roleRepository = new RoleRepository();
	}

	public CompletableFuture<List<MHomeScreenButton>> homeScreenButtons(DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		return roleRepository.isAdmin(Env.getAD_Role_ID(idempiereContext), environment)
				.thenApply((isAdmin) -> dashboardButtonGroupButtonRepository.get(isAdmin, idempiereContext));
	}
}