package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.model.input.ProductCategoryInput;
import org.compiere.util.Env;

import java.util.List;

public class ProductCategoryRepository extends BaseRepository<MProductCategory_BH, ProductCategoryInput> {
	@Override
	protected MProductCategory_BH createModelInstance() {
		return new MProductCategory_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MProductCategory_BH mapInputModelToModel(ProductCategoryInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<MProductCategory_BH> get() {
		return getBaseQuery(MProductCategory_BH.COLUMNNAME_BH_Product_Category_Type + " IS NOT NULL")
				.list();
	}
}
