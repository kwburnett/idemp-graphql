package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.graphql.repository.UserRepository;

public class UserMutation implements GraphQLMutationResolver {
	private final UserRepository userRepository;

	public UserMutation() {
		userRepository = new UserRepository();
	}

	public boolean acceptTermsOfService(boolean accept) {
		return (boolean) userRepository.acceptTermsOfUse().getBH_HasAcceptedTermsOfUse();
	}
}
