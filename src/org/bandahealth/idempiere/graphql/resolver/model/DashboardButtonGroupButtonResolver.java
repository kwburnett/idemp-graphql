package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MDashboardButtonGroupButton;

/**
 * The Dashboard Button Group Button resolver containing specific methods to fetch non-standard iDempiere properties
 * for the consumer
 */
public class DashboardButtonGroupButtonResolver extends BaseResolver<MDashboardButtonGroupButton>
		implements GraphQLResolver<MDashboardButtonGroupButton> {

	public int lineNumber(MDashboardButtonGroupButton entity) {
		return entity.getLineNo();
	}
}
