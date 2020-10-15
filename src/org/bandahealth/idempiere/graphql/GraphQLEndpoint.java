package org.bandahealth.idempiere.graphql;

import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.context.AuthGraphQLContextBuilder;
import org.bandahealth.idempiere.graphql.error.ErrorHandler;
import org.bandahealth.idempiere.graphql.resolver.*;
import org.compiere.util.CLogger;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

public class GraphQLEndpoint extends GraphQLHttpServlet {

	private final CLogger logger = CLogger.getCLogger(GraphQLEndpoint.class);

	@Override
	protected GraphQLConfiguration getConfiguration() {
		return GraphQLConfiguration.with(createContext()).with(createObjectMapper()).build();
	}

	/**
	 * Generates the schema from the appropriate SDL files, then adds the appropriate resolvers
	 *
	 * @return The schema to use in this GraphQL plugin
	 */
	private GraphQLSchema createSchema() {
		return SchemaParser.newParser()
				.file("WEB-INF/resources/schema.graphqls")
				.resolvers(
						new Query(), new Mutation(), new UserResolver(), new ClientResolver(),
						new OrganizationResolver(), new RoleResolver(), new WarehouseResolver()
				)
				.build()
				.makeExecutableSchema();
	}

	/**
	 * Generates the right object and ensures the custom context builder is used
	 *
	 * @return The object to use in the GraphQL configuration
	 */
	private GraphQLInvocationInputFactory createContext() {
		return GraphQLInvocationInputFactory.newBuilder(createSchema())
				.withGraphQLContextBuilder(AuthGraphQLContextBuilder::new)
				.build();
	}

	/**
	 * Ensure we provide some error handling of our own
	 *
	 * @return The object mapper to use in the GraphQL configuration
	 */
	private GraphQLObjectMapper createObjectMapper() {
		return GraphQLObjectMapper.newBuilder()
				.withGraphQLErrorHandler(new ErrorHandler())
				.build();
	}
}
