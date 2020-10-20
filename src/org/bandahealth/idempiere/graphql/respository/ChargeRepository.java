package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.compiere.util.Env;

public class ChargeRepository extends BaseRepository<MCharge_BH> {
	@Override
	public MCharge_BH getModelInstance() {
		return new MCharge_BH(Env.getCtx(), 0, null);
	}
}
