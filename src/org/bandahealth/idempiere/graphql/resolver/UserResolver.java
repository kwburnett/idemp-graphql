package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MUser_BH;

public class UserResolver extends BaseResolver<MUser_BH> implements GraphQLResolver<MUser_BH> {

	public boolean hasAcceptedTermsOfUse(MUser_BH entity) {
		return (boolean) entity.getBH_HasAcceptedTermsOfUse();
	}
}
