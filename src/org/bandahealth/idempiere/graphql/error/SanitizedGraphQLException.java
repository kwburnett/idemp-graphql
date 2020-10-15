package org.bandahealth.idempiere.graphql.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ExceptionWhileDataFetching;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;
import org.adempiere.exceptions.AdempiereException;

import java.util.Arrays;

public class SanitizedGraphQLException extends ExceptionWhileDataFetching {
	public SanitizedGraphQLException(ExecutionPath path, Throwable exception, SourceLocation sourceLocation) {
		super(path, exception, sourceLocation);
	}

	public SanitizedGraphQLException(ExceptionWhileDataFetching error) {
		this(ExecutionPath.fromList(error.getPath()), error.getException(), error.getLocations().get(0));
	}

	public SanitizedGraphQLException(AdempiereException error) {
		this(ExecutionPath.fromList(Arrays.asList(error.getStackTrace())), error, new SourceLocation(-1, -1));
	}

	@Override
	@JsonIgnore
	public Throwable getException() {
		return super.getException();
	}
}
