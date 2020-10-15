package org.bandahealth.idempiere.graphql;

import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory;
import org.compiere.util.CLogger;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

public class GraphQLEndpoint extends GraphQLHttpServlet {

	private final CLogger logger = CLogger.getCLogger(GraphQLEndpoint.class);

	@Override
	protected GraphQLConfiguration getConfiguration() {
		return GraphQLConfiguration.with(createContext()).build();
	}

	private GraphQLSchema createSchema() {
		LinkRepository linkRepository = new LinkRepository();
		return SchemaParser.newParser()
				.file("WEB-INF/resources/schema.graphqls")
				.resolvers(new Query(linkRepository))
				.build()
				.makeExecutableSchema();
	}

	private GraphQLInvocationInputFactory createContext() {
		return GraphQLInvocationInputFactory.newBuilder(createSchema())
				.withGraphQLContextBuilder(AuthGraphQLContextBuilder::new)
				.build();
	}
}
