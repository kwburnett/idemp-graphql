package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.model.input.ProductCategoryInput;

import java.util.List;
import java.util.Properties;

public class ProductCategoryRepository extends BaseRepository<MProductCategory_BH, ProductCategoryInput> {
	@Override
	protected MProductCategory_BH createModelInstance(Properties idempiereContext) {
		return new MProductCategory_BH(idempiereContext, 0, null);
	}

	@Override
	public MProductCategory_BH mapInputModelToModel(ProductCategoryInput entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public List<MProductCategory_BH> get(Properties idempiereContext) {
		return getBaseQuery(idempiereContext, MProductCategory_BH.COLUMNNAME_BH_Product_Category_Type +
				" IS NOT NULL").list();
	}
}
