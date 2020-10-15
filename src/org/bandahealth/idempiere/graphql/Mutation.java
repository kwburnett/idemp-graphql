package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.model.AuthData;
import org.bandahealth.idempiere.graphql.model.AuthResponse;
import org.bandahealth.idempiere.graphql.respository.UserRepository;

public class Mutation implements GraphQLMutationResolver {

	private final UserRepository userRepository;

	public Mutation() {
		this.userRepository = new UserRepository();
	}

	public AuthResponse signIn(AuthData credentials, DataFetchingEnvironment environment) {
		return userRepository.signIn(credentials, environment.getContext());
	}
}
