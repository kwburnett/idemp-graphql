package org.bandahealth.idempiere.graphql.directive;

import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.bandahealth.idempiere.graphql.utils.DateUtil;

import java.sql.Timestamp;

/**
 * A directive that formats timestamps to be strings for the API consumer
 */
public class DateFormatDirective implements SchemaDirectiveWiring {

	// This should match what's defined in the schema.graphqls SDL
	public static final String DATE_FORMAT_DIRECTIVE_NAME = "dateFormat";
	private static final String DATE_FORMAT_DIRECTIVE_FORMAT_ARGUMENT_NAME = "format";

	@Override
	public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
		GraphQLFieldDefinition field = environment.getElement();
		GraphQLFieldsContainer parentType = environment.getFieldsContainer();

		// DataFetcherFactories.wrapDataFetcher is a helper to wrap data fetchers so that CompletionStage is handled
		// correctly along with POJOs
		DataFetcher originalFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
		DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(originalFetcher, ((dataFetchingEnvironment, value) -> {
			String format = dataFetchingEnvironment.getArgument(DATE_FORMAT_DIRECTIVE_FORMAT_ARGUMENT_NAME);
			if (value instanceof Timestamp) {
				return DateUtil.parseDateOnly((Timestamp) value, format);
			}
			return value;
		}));

		// This will extend the field by adding a new "format" argument to it for the date formatting which allows clients
		// to opt into that as well as wrapping the base data fetcher so it performs the formatting over top of the base
		// values.
		FieldCoordinates coordinates = FieldCoordinates.coordinates(parentType, field);
		environment.getCodeRegistry().dataFetcher(coordinates, dataFetcher);

		return field.transform(builder -> builder
				.argument(GraphQLArgument
						.newArgument()
						.name(DATE_FORMAT_DIRECTIVE_FORMAT_ARGUMENT_NAME)
						.type(Scalars.GraphQLString)
						.defaultValue(DateUtil.DATE_FORMAT)
				)
		);
	}
}
