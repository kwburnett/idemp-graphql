package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.UserRepository;

public class UserQuery implements GraphQLQueryResolver {
	private final UserRepository userRepository;

	public UserQuery() {
		userRepository = new UserRepository();
	}

	public MUser_BH acceptTermsOfUse(DataFetchingEnvironment environment) {
		return userRepository.acceptTermsOfUse(BandaGraphQLContext.getCtx(environment));
	}
}
