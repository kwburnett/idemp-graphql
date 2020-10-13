package org.bandahealth.idempiere.graphql;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "HelloServlet", urlPatterns = {"graphql/*"}, loadOnStartup = 1)
public class GraphQLEndpoint extends GraphQLHttpServlet {

	@Override
	protected GraphQLConfiguration getConfiguration() {
		return GraphQLConfiguration.with(createSchema()).build();
	}

	private GraphQLSchema createSchema() {
		LinkRepository linkRepository = new LinkRepository();
		return SchemaParser.newParser()
				.file("schema.graphqls")
				.resolvers(new Query(linkRepository))
				.build()
				.makeExecutableSchema();
	}
}
