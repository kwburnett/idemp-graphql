package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MHomeScreenButton;

public class HomeScreenButtonResolver extends BaseResolver<MHomeScreenButton>
		implements GraphQLResolver<MHomeScreenButton> {

	public int lineNumber(MHomeScreenButton entity) {
		return entity.getLineNo();
	}
}
