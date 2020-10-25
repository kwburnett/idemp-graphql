package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.SchemaParserBuilder;

public class Mutation {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(new AuthenticationMutation(), new OrderMutation());
	}
}
