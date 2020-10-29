package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaQueryComposer implements GraphQLQueryResolver {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new OrderQuery(),
				new ProcessQuery(),
				new ProductQuery(),
				new StorageOnHandQuery()
		);
	}
}
