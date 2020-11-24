package org.bandahealth.idempiere.graphql.resolver.mutation;

import graphql.kickstart.tools.SchemaParserBuilder;

/**
 * This class is responsible for adding all mutation resolvers to the GraphQL SchemaParserBuilder.
 */
public class BandaMutationComposer {
	/**
	 * Add the all mutations to the builder.
	 *
	 * @param builder The builder for the GraphQL SchemaParserBuilder
	 */
	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AuthenticationMutation(),
				new BusinessPartnerMutation(),
				new ChargeMutation(),
				new InvoiceMutation(),
				new OrderMutation(),
				new PaymentMutation(),
				new ProductMutation(),
				new StorageOnHandMutation(),
				new UserMutation()
		);
	}
}
