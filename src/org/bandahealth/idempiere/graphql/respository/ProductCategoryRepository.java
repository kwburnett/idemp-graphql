package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.compiere.util.Env;

public class ProductCategoryRepository extends BaseRepository<MProductCategory_BH> {
	@Override
	public MProductCategory_BH getModelInstance() {
		return new MProductCategory_BH(Env.getCtx(), 0, null);
	}
}
