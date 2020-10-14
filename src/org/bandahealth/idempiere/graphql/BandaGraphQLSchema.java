package org.bandahealth.idempiere.graphql;

import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

public class BandaGraphQLSchema {
	public GraphQLSchema schema() {
		LinkRepository linkRepository = new LinkRepository();
		return SchemaParser.newParser()
				.file("schema.graphqls")
				.resolvers(new Query(linkRepository))
				.build()
				.makeExecutableSchema();
	}
}
