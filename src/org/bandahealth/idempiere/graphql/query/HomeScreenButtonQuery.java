package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MHomeScreenButton;
import org.bandahealth.idempiere.graphql.repository.HomeScreenButtonRepository;
import org.bandahealth.idempiere.graphql.repository.RoleRepository;
import org.compiere.util.Env;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeScreenButtonQuery implements GraphQLQueryResolver {
	private final HomeScreenButtonRepository homeScreenButtonRepository;
	private final RoleRepository roleRepository;

	public HomeScreenButtonQuery() {
		homeScreenButtonRepository = new HomeScreenButtonRepository();
		roleRepository = new RoleRepository();
	}

	public CompletableFuture<List<MHomeScreenButton>> homeScreenButtons(DataFetchingEnvironment environment) {
		return roleRepository.isAdmin(Env.getAD_Role_ID(Env.getCtx()), environment)
				.thenApply(homeScreenButtonRepository::get);
	}
}
