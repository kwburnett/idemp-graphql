package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.SchemaParserBuilder;

public class Resolver {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AccountResolver(),
				new AttributeSetInstanceResolver(),
				new AttributeSetResolver(),
				new BusinessPartnerResolver(),
				new ChargeResolver(),
				new ClientResolver(),
				new FormResolver(),
				new LocationResolver(),
				new OrderLineResolver(),
				new OrderResolver(),
				new OrganizationResolver(),
				new PaymentResolver(),
				new ProcessParameterResolver(),
				new ProcessResolver(),
				new ProductCategoryResolver(),
				new ProductResolver(),
				new ReferenceListResolver(),
				new ReferenceResolver(),
				new ReportViewResolver(),
				new RoleResolver(),
				new UserResolver(),
				new WarehouseResolver(),
				new WorkflowResolver()
		);
	}
}
