package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.model.AuthenticationData;
import org.bandahealth.idempiere.graphql.model.AuthenticationResponse;
import org.bandahealth.idempiere.graphql.respository.UserRepository;

public class Mutation implements GraphQLMutationResolver {

	private final UserRepository userRepository;

	public Mutation() {
		this.userRepository = new UserRepository();
	}

	public AuthenticationResponse signIn(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return userRepository.signIn(credentials, environment.getContext());
	}

	public AuthenticationResponse changePassword(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return userRepository.changePassword(credentials, environment.getContext());
	}
}
