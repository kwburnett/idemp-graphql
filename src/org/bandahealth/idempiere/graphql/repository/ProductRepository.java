package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.compiere.util.Env;

public class ProductRepository extends BaseRepository<MProduct_BH> {
	@Override
	public MProduct_BH getModelInstance() {
		return new MProduct_BH(Env.getCtx(), 0, null);
	}
}
