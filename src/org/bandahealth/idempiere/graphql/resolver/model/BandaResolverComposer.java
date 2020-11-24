package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.SchemaParserBuilder;

/**
 * This class is responsible for adding all non-query & non-mutation resolvers to the GraphQL SchemaParserBuilder.
 */
public class BandaResolverComposer {
	/**
	 * Add all resolvers to the GraphQL SchemaParserBuilder
	 *
	 * @param builder The builder for the GraphQL Schema Parser
	 */
	public static void addAll(SchemaParserBuilder builder) {
		builder.resolvers(
				new AccountResolver(),
				new AttributeSetInstanceResolver(),
				new AttributeSetResolver(),
				new BusinessPartnerResolver(),
				new ChargeResolver(),
				new ChargeTypeResolver(),
				new ClientResolver(),
				new DashboardButtonGroupButtonResolver(),
				new FormResolver(),
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
