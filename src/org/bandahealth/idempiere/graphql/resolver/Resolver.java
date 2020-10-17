package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.SchemaParserBuilder;

public class Resolver {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(new UserResolver(), new ClientResolver(), new OrganizationResolver(), new RoleResolver(),
				new WarehouseResolver());
	}
}
