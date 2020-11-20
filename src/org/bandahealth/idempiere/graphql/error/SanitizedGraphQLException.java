package org.bandahealth.idempiere.graphql.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ExceptionWhileDataFetching;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;
import org.adempiere.exceptions.AdempiereException;

import java.util.Arrays;

/**
 * This allows us to return whatever exception that is thrown by the GraphQL plug-in, but to only return the message
 * of the error and nothing else to the application
 */
public class SanitizedGraphQLException extends ExceptionWhileDataFetching {
	/**
	 * Default constructor for extending ExceptionWhileDataFetching
	 *
	 * @param path           The GraphQL path of the error
	 * @param exception      The exception that was thrown
	 * @param sourceLocation Where in the code this error originated
	 */
	public SanitizedGraphQLException(ExecutionPath path, Throwable exception, SourceLocation sourceLocation) {
		super(path, exception, sourceLocation);
	}

	/**
	 * The constructor to allow the default GraphQL error to be dealt with
	 *
	 * @param error The default GraphQL error type of what error was ound
	 */
	public SanitizedGraphQLException(ExceptionWhileDataFetching error) {
		this(ExecutionPath.fromList(error.getPath()), error.getException(), error.getLocations().get(0));
	}

	/**
	 * The wrapper constructor for any of our errors that were thrown
	 *
	 * @param error The iDempiere default error class
	 */
	public SanitizedGraphQLException(AdempiereException error) {
		this(ExecutionPath.fromList(Arrays.asList(error.getStackTrace())), error, new SourceLocation(-1, -1));
	}

	/**
	 * We don't want this property to be serialized for the consumer to ensure that too much data doesn't get passed
	 * to the consumer, so we extend the property from the super-class and add @JsonIgnore
	 *
	 * @return The exception that was thrown
	 */
	@Override
	@JsonIgnore
	public Throwable getException() {
		return super.getException();
	}
}
