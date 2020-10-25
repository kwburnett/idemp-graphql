package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.model.input.ProductCategoryInput;
import org.compiere.util.Env;

public class ProductCategoryRepository extends BaseRepository<MProductCategory_BH, ProductCategoryInput> {
	@Override
	public MProductCategory_BH getModelInstance() {
		return new MProductCategory_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MProductCategory_BH save(ProductCategoryInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
