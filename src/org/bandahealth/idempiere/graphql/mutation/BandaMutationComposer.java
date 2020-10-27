package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaMutationComposer {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AuthenticationMutation(),
				new OrderMutation(),
				new ProductMutation()
		);
	}
}
