package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MCharge_BH;

public class ChargeResolver extends BaseResolver<MCharge_BH> implements GraphQLResolver<MCharge_BH> {

	public boolean isLocked(MCharge_BH entity) {
		return entity.isBH_Locked();
	}

	public int accountId(MCharge_BH entity) {
		return entity.getC_ElementValue_ID();
	}
}
