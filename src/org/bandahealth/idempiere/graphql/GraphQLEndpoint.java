package org.bandahealth.idempiere.graphql;

import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.kickstart.execution.GraphQLObjectMapper;
import graphql.kickstart.execution.GraphQLQueryInvoker;
import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory;
import graphql.kickstart.tools.*;
import org.bandahealth.idempiere.graphql.cache.BandaCache;
import org.bandahealth.idempiere.graphql.cache.CacheFactory;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContextBuilder;
import org.bandahealth.idempiere.graphql.directive.BandaDirectiveComposer;
import org.bandahealth.idempiere.graphql.error.ErrorHandler;
import org.bandahealth.idempiere.graphql.mutation.BandaMutationComposer;
import org.bandahealth.idempiere.graphql.query.BandaQueryComposer;
import org.bandahealth.idempiere.graphql.resolver.*;
import org.bandahealth.idempiere.graphql.scalar.BandaScalarComposer;
import org.compiere.util.CLogger;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.schema.GraphQLSchema;

import java.util.ArrayList;
import java.util.List;

public class GraphQLEndpoint extends GraphQLHttpServlet {
	private static final CacheFactory cacheFactory = new CacheFactory();

	public static BandaCache<Object, Object> getCache(Class<?> clazz) {
		return cacheFactory.getCache(clazz);
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
		List<Instrumentation> instrumentationList = new ArrayList<>();
		instrumentationList.add(new MaxQueryDepthInstrumentation(6));
//		instrumentationList.add(new TracingInstrumentation());
//		instrumentationList.add(dispatcherInstrumentation);

		return GraphQLQueryInvoker.newBuilder()
				.withPreparsedDocumentProvider(preparsedCache)
				.withInstrumentation(new ChainedInstrumentation(instrumentationList))
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
						"WEB-INF/resources/charge-type.graphqls",
						"WEB-INF/resources/client.graphqls",
						"WEB-INF/resources/element.graphqls",
						"WEB-INF/resources/form.graphqls",
						"WEB-INF/resources/home-screen-button.graphqls",
						"WEB-INF/resources/invoice.graphqls",
						"WEB-INF/resources/invoice-line.graphqls",
						"WEB-INF/resources/location.graphqls",
						"WEB-INF/resources/locator.graphqls",
						"WEB-INF/resources/order.graphqls",
						"WEB-INF/resources/order-line.graphqls",
						"WEB-INF/resources/order-status.graphqls",
						"WEB-INF/resources/organization.graphqls",
						"WEB-INF/resources/payment.graphqls",
						"WEB-INF/resources/process.graphqls",
						"WEB-INF/resources/process-info.graphqls",
						"WEB-INF/resources/process-info-parameter.graphqls",
						"WEB-INF/resources/process-instance.graphqls",
						"WEB-INF/resources/process-parameter.graphqls",
						"WEB-INF/resources/product.graphqls",
						"WEB-INF/resources/product-category.graphqls",
						"WEB-INF/resources/record.graphqls",
						"WEB-INF/resources/reference.graphqls",
						"WEB-INF/resources/reference-list.graphqls",
						"WEB-INF/resources/report-output.graphqls",
						"WEB-INF/resources/report-view.graphqls",
						"WEB-INF/resources/role.graphqls",
						"WEB-INF/resources/storage-on-hand.graphqls",
						"WEB-INF/resources/table.graphqls",
						"WEB-INF/resources/user.graphqls",
						"WEB-INF/resources/value-rule.graphqls",
						"WEB-INF/resources/warehouse.graphqls",
						"WEB-INF/resources/workflow.graphqls"
				);
		BandaQueryComposer.addAll(builder);
		BandaMutationComposer.addAll(builder);
		BandaResolverComposer.addAll(builder);
		BandaDirectiveComposer.addAll(builder);
		BandaScalarComposer.addAll(builder);
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
