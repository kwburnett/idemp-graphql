package org.bandahealth.idempiere.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MDashboardButtonGroupButton;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.DashboardButtonGroupButtonRepository;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.util.Env;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class DashboardButtonGroupButtonQuery implements GraphQLQueryResolver {
	private final DashboardButtonGroupButtonRepository dashboardButtonGroupButtonRepository;
	private final RoleRepository roleRepository;

	public DashboardButtonGroupButtonQuery() {
		dashboardButtonGroupButtonRepository = new DashboardButtonGroupButtonRepository();
		roleRepository = new RoleRepository();
	}

	public CompletableFuture<List<MDashboardButtonGroupButton>> dashboardButtonGroupButtons(
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		return roleRepository.isAdmin(Env.getAD_Role_ID(idempiereContext), environment)
				.thenApply((isAdmin) -> dashboardButtonGroupButtonRepository.get(isAdmin, idempiereContext));
	}
}
