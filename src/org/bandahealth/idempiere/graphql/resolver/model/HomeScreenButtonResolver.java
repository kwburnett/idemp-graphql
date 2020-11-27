package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MHomeScreenButton;

/**
 * The Home Screen Button resolver containing specific methods to fetch non-standard iDempiere properties for the
 * consumer
 */
public class HomeScreenButtonResolver extends BaseResolver<MHomeScreenButton>
		implements GraphQLResolver<MHomeScreenButton> {

	public int lineNumber(MHomeScreenButton entity) {
		return entity.getLineNo();
	}
}
