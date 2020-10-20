package org.bandahealth.idempiere.graphql;

import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.GraphQLQueryInvoker;
import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory;
import graphql.kickstart.tools.SchemaParserBuilder;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContextBuilder;
import org.bandahealth.idempiere.graphql.error.ErrorHandler;
import org.bandahealth.idempiere.graphql.mutation.Mutation;
import org.bandahealth.idempiere.graphql.query.Query;
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
		return GraphQLConfiguration.with(createContext()).with(createObjectMapper()).with(getInvoker()).build();
	}

	/**
	 * Allows for the invoked queries to return timing information for debugging and performance-tuning.
	 *
	 * @return The configured query invoker
	 */
	public GraphQLQueryInvoker getInvoker() {
		DataLoaderDispatcherInstrumentationOptions options = DataLoaderDispatcherInstrumentationOptions
				.newOptions().includeStatistics(true);

		DataLoaderDispatcherInstrumentation dispatcherInstrumentation
				= new DataLoaderDispatcherInstrumentation(options);
		return GraphQLQueryInvoker.newBuilder()
				.withInstrumentation(dispatcherInstrumentation)
				.build();
	}

	/**
	 * Generates the schema from the appropriate SDL files, then adds the appropriate resolvers
	 *
	 * @return The schema to use in this GraphQL plugin
	 */
	private GraphQLSchema createSchema() {
		SchemaParserBuilder builder = SchemaParser.newParser()
				.files(
						"WEB-INF/resources/schema.graphqls",
						"WEB-INF/resources/authentication.graphqls",
						"WEB-INF/resources/business-partner.graphqls",
						"WEB-INF/resources/charge.graphqls",
						"WEB-INF/resources/document.graphqls",
						"WEB-INF/resources/location.graphqls",
						"WEB-INF/resources/order.graphqls",
						"WEB-INF/resources/order-line.graphqls",
						"WEB-INF/resources/order-status.graphqls",
						"WEB-INF/resources/payment.graphqls",
						"WEB-INF/resources/product.graphqls",
						"WEB-INF/resources/reference-list.graphqls"
				);
		Query.addAll(builder);
		Mutation.addAll(builder);
		Resolver.addAll(builder);
		return builder
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
				.withGraphQLContextBuilder(BandaGraphQLContextBuilder::new)
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
