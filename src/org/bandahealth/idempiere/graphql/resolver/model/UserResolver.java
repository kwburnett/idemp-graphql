package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MUser_BH;

/**
 * The User resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class UserResolver extends BaseResolver<MUser_BH> implements GraphQLResolver<MUser_BH> {

	public boolean hasAcceptedTermsOfUse(MUser_BH entity) {
		return (boolean) entity.getBH_HasAcceptedTermsOfUse();
	}
}
