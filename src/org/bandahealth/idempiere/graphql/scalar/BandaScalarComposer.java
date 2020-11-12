package org.bandahealth.idempiere.graphql.scalar;

import graphql.kickstart.tools.SchemaParserBuilder;

/**
 * This class is responsible for adding all custom scalars to the GraphQL API
 */
public class BandaScalarComposer {
	/**
	 * Add all custom scalars to the GraphQL SchemaParserBuilder
	 *
	 * @param builder The builder for the GraphQL Schema Parser
	 */
	public static void addAll(SchemaParserBuilder builder) {
		builder.scalars(
				ObjectScalar.Object
		);
	}
}
