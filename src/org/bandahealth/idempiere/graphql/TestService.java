package org.bandahealth.idempiere.graphql;

import javax.ws.rs.GET;

public class TestService {

	@GET
	public String test() {
		return "hello";
	}
}
