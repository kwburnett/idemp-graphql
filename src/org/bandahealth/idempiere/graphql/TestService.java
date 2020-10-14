package org.bandahealth.idempiere.graphql;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class TestService {

	@GET
	public String test() {
		return "hello";
	}
}
