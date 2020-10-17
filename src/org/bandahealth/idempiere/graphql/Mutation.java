package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.model.AuthenticationData;
import org.bandahealth.idempiere.graphql.model.AuthenticationResponse;
import org.bandahealth.idempiere.graphql.respository.AuthenticationRepository;
import org.bandahealth.idempiere.graphql.respository.UserRepository;

public class Mutation implements GraphQLMutationResolver {

	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;

	public Mutation() {
		this.userRepository = new UserRepository();
		this.authenticationRepository = new AuthenticationRepository();
	}

	public AuthenticationResponse signIn(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.signIn(credentials, environment.getContext());
	}

	public AuthenticationResponse changePassword(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.changePassword(credentials, environment.getContext());
	}
}
