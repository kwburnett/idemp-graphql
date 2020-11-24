package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.SchemaParserBuilder;

/**
 * This class is responsible for adding all query resolvers to the GraphQL SchemaParserBuilder.
 */
public class BandaQueryComposer implements GraphQLQueryResolver {
	/**
	 * Add the all queries to the builder.
	 *
	 * @param builder The builder for the GraphQL Schema Parser
	 */
	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AccountQuery(),
				new BusinessPartnerQuery(),
				new ChargeQuery(),
				new DashboardButtonGroupButtonQuery(),
				new InvoiceQuery(),
				new OrderQuery(),
				new PaymentQuery(),
				new ProcessQuery(),
				new ProductCategoryQuery(),
				new ProductQuery(),
				new ReferenceListQuery(),
				new StorageOnHandQuery(),
				new UserQuery()
		);
	}
}
