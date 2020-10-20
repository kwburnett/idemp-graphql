package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.compiere.util.Env;

public class OrderLineRepository extends BaseRepository<MOrderLine_BH> {
	@Override
	public MOrderLine_BH getModelInstance() {
		return new MOrderLine_BH(Env.getCtx(), 0, null);
	}
}
