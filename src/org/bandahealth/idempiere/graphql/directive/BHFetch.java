package org.bandahealth.idempiere.graphql.directive;

import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

public class BHFetch implements SchemaDirectiveWiring {

	public static final String NAME = "bhFetch";

	@Override
	public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
		String targetField = (String) environment.getDirective().getArgument("from").getValue();

		GraphQLFieldDefinition field = environment.getElement();
		GraphQLFieldsContainer parentType = environment.getFieldsContainer();

		// Get the property name to help determine what to fetch
		DataFetcher originalFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
		DataFetcher customDataFetcher = new DataFetcher() {
			@Override
			public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
				return PropertyDataFetcherHelper.getPropertyValue(targetField, dataFetchingEnvironment.getSource(),
						field.getType(), dataFetchingEnvironment);
			}
		};

		// Change the field definition to fetch the other type
		environment.getCodeRegistry().dataFetcher(parentType, field, customDataFetcher);
		return field;
	}
}
