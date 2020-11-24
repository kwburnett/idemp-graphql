package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.UserRepository;

/**
 * Handle all mutations relating to users
 */
public class UserMutation implements GraphQLMutationResolver {
	private final UserRepository userRepository;

	public UserMutation() {
		userRepository = new UserRepository();
	}

	/**
	 * Allow a user to update whether they're accepting the terms of service or not
	 *
	 * @param accept      Whether the user accepts the terms of service or not
	 * @param environment The environment associated with all calls, containing context
	 * @return The saved value of whether the user accepted the terms of service or not
	 */
	public boolean acceptTermsOfService(boolean accept, DataFetchingEnvironment environment) {
		return (boolean) userRepository.acceptTermsOfUse(BandaGraphQLContext.getCtx(environment))
				.getBH_HasAcceptedTermsOfUse();
	}
}
