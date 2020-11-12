package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.AuthenticationData;
import org.bandahealth.idempiere.graphql.model.AuthenticationResponse;
import org.bandahealth.idempiere.graphql.repository.AuthenticationRepository;

/**
 * Handle all mutations relating to authentication
 */
public class AuthenticationMutation implements GraphQLMutationResolver {
	private final AuthenticationRepository authenticationRepository;

	public AuthenticationMutation() {
		this.authenticationRepository = new AuthenticationRepository();
	}

	/**
	 * The sign-in method to authentication a user
	 *
	 * @param credentials The login information passed in by a user.
	 * @param environment The environment associated with all calls, containing context.
	 * @return An appropriate response containing a JWT token and user information.
	 */
	public AuthenticationResponse signIn(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.signIn(credentials, BandaGraphQLContext.getCtx(environment));
	}

	/**
	 * The method that allows a user to change their password.
	 *
	 * @param credentials The login and change-password information passed in by a user.
	 * @param environment The environment associated with all calls, containing context.
	 * @return An appropriate response containing a JWT token and user information.
	 */
	public AuthenticationResponse changePassword(AuthenticationData credentials, DataFetchingEnvironment environment) {
		return authenticationRepository.changePassword(credentials, BandaGraphQLContext.getCtx(environment));
	}
}