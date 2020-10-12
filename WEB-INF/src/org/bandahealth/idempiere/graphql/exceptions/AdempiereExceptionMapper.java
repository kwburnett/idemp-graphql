package org.bandahealth.idempiere.graphql.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.adempiere.exceptions.AdempiereException;

/**
 * Have a nice way to return exceptions
 * 
 * @author andrew
 *
 */
public class AdempiereExceptionMapper implements ExceptionMapper<AdempiereException> {

	@Override
	public Response toResponse(AdempiereException exception) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.header("Exception", exception.getMessage()).build();
	}

}
