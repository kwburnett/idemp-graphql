package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.AuthenticationData;
import org.bandahealth.idempiere.graphql.model.AuthenticationResponse;
import org.bandahealth.idempiere.graphql.repository.AuthenticationRepository;

public class AuthenticationMutation implements GraphQLMutationResolver {

	private final AuthenticationRepository authenticationRepository;

	public AuthenticationMutation() {
		this.authenticationRepository = new AuthenticationRepository();
	}

	public AuthenticationResponse signIn(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.signIn(credentials, BandaGraphQLContext.getCtx(environment));
	}

	public AuthenticationResponse changePassword(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.changePassword(credentials, BandaGraphQLContext.getCtx(environment));
	}
}