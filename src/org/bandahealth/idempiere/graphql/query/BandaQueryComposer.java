package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaQueryComposer implements GraphQLQueryResolver {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AccountQuery(),
				new BusinessPartnerQuery(),
				new ChargeQuery(),
				new HomeScreenButtonQuery(),
				new InvoiceQuery(),
				new OrderQuery(),
				new PaymentQuery(),
				new ProcessQuery(),
				new ProductQuery(),
				new StorageOnHandQuery(),
				new UserQuery()
		);
	}
}
