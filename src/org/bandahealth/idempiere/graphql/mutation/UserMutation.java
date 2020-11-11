package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.UserRepository;

public class UserMutation implements GraphQLMutationResolver {
	private final UserRepository userRepository;

	public UserMutation() {
		userRepository = new UserRepository();
	}

	public boolean acceptTermsOfService(boolean accept, DataFetchingEnvironment environment) {
		return (boolean) userRepository.acceptTermsOfUse(BandaGraphQLContext.getCtx(environment))
				.getBH_HasAcceptedTermsOfUse();
	}
}
