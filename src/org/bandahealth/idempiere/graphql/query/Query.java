package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.SchemaParserBuilder;

public class Query implements GraphQLQueryResolver {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new OrderQuery(),
				new ProductQuery()
		);
	}
}
