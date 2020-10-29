package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.SchemaParserBuilder;

public class BandaResolverComposer {

	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AccountResolver(),
				new AttributeSetInstanceResolver(),
				new AttributeSetResolver(),
				new BusinessPartnerResolver(),
				new ChargeResolver(),
				new ChargeTypeResolver(),
				new ClientResolver(),
				new FormResolver(),
				new HomeScreenButtonResolver(),
				new InvoiceLineResolver(),
				new InvoiceResolver(),
				new LocationResolver(),
				new LocatorResolver(),
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
				new StorageOnHandResolver(),
				new UserResolver(),
				new WarehouseResolver(),
				new WorkflowResolver()
		);
	}
}
