package org.bandahealth.idempiere.graphql.error;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import org.adempiere.exceptions.AdempiereException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The custom error handler to use with all GraphQL queries
 */
public class ErrorHandler extends DefaultGraphQLErrorHandler {
	@Override
	protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
		return errors.stream()
				.filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e) || e instanceof AdempiereException)
				.map(e ->
						e instanceof ExceptionWhileDataFetching ? new SanitizedGraphQLException((ExceptionWhileDataFetching) e) : (
								e instanceof AdempiereException ? new SanitizedGraphQLException((AdempiereException) e) : e
						)
				)
				.collect(Collectors.toList());
	}
}
