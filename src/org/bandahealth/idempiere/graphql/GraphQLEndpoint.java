package org.bandahealth.idempiere.graphql;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.GraphQLQueryInvoker;
import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory;
import graphql.kickstart.tools.SchemaParserBuilder;
import org.bandahealth.idempiere.graphql.cache.BandaCache;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContextBuilder;
import org.bandahealth.idempiere.graphql.directive.Directive;
import org.bandahealth.idempiere.graphql.error.ErrorHandler;
import org.bandahealth.idempiere.graphql.mutation.Mutation;
import org.bandahealth.idempiere.graphql.query.Query;
import org.bandahealth.idempiere.graphql.resolver.*;
import org.compiere.util.CLogger;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

import java.util.HashMap;
import java.util.Map;

public class GraphQLEndpoint extends GraphQLHttpServlet {

	private static final Map<String, BandaCache<Object, Object>> cacheMap = new HashMap<>();

	public static BandaCache<Object, Object> getCache(Class<?> clazz) {
		BandaCache<Object, Object> classCache = cacheMap.get(clazz.getName());
		if (classCache == null) {
			classCache = new BandaCache<>();
			cacheMap.put(clazz.getName(), classCache);
		}
		return classCache;
	}

	private final CLogger logger = CLogger.getCLogger(GraphQLEndpoint.class);

	@Override
	protected GraphQLConfiguration getConfiguration() {
		logger.fine("Getting GraphQL endpoint config");
		return GraphQLConfiguration.with(createContext()).with(createObjectMapper()).with(getInvoker()).build();
	}

	/**
	 * Allows for the invoked queries to return timing information for debugging and performance-tuning.
	 *
	 * @return The configured query invoker
	 */
	private GraphQLQueryInvoker getInvoker() {
		DataLoaderDispatcherInstrumentationOptions options = DataLoaderDispatcherInstrumentationOptions
				.newOptions().includeStatistics(true);

		// Set up the cache so, if a GraphQL query has been passed in before, it doesn't have to be parsed
		// again before heading to the DB (note, this cache doesn't store DB query results)
		PreparsedDocumentProvider preparsedCache = (executionInput, computeFunction) -> {
			BandaCache<Object, Object> cache = GraphQLEndpoint.getCache(PreparsedDocumentEntry.class);
			PreparsedDocumentEntry preparsedDocumentEntry = (PreparsedDocumentEntry) cache.get(executionInput.getQuery());
			if (preparsedDocumentEntry == null) {
				preparsedDocumentEntry = computeFunction.apply(executionInput);
				cache.set(executionInput.getQuery(), preparsedDocumentEntry);
			}
			return preparsedDocumentEntry;
		};

		Instrumentation dispatcherInstrumentation
				= new DataLoaderDispatcherInstrumentation(options);
		return GraphQLQueryInvoker.newBuilder()
				.withPreparsedDocumentProvider(preparsedCache)
//				.withInstrumentation(new TracingInstrumentation())
//				.withInstrumentation(dispatcherInstrumentation)
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
						"WEB-INF/resources/account.graphqls",
						"WEB-INF/resources/attribute-set.graphqls",
						"WEB-INF/resources/attribute-set-instance.graphqls",
						"WEB-INF/resources/authentication.graphqls",
						"WEB-INF/resources/business-partner.graphqls",
						"WEB-INF/resources/charge.graphqls",
						"WEB-INF/resources/client.graphqls",
						"WEB-INF/resources/document.graphqls",
						"WEB-INF/resources/location.graphqls",
						"WEB-INF/resources/order.graphqls",
						"WEB-INF/resources/order-line.graphqls",
						"WEB-INF/resources/order-status.graphqls",
						"WEB-INF/resources/organization.graphqls",
						"WEB-INF/resources/payment.graphqls",
						"WEB-INF/resources/product.graphqls",
						"WEB-INF/resources/product-category.graphqls",
						"WEB-INF/resources/reference-list.graphqls",
						"WEB-INF/resources/role.graphqls",
						"WEB-INF/resources/user.graphqls",
						"WEB-INF/resources/warehouse.graphqls"
				);
		Query.addAll(builder);
		Mutation.addAll(builder);
		Resolver.addAll(builder);
		Directive.addAll(builder);
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
