package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.compiere.util.Env;

public class BusinessPartnerRepository extends BaseRepository<MBPartner_BH> {
	@Override
	public MBPartner_BH getModelInstance() {
		return new MBPartner_BH(Env.getCtx(), 0, null);
	}
}
