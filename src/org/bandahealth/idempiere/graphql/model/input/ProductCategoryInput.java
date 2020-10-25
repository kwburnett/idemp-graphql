package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ProductCategoryInput extends MProductCategory_BH {
	public ProductCategoryInput() {
		super(Env.getCtx(), 0, null);
	}

	public ProductCategoryInput(Properties ctx, int M_Product_Category_ID, String trxName) {
		super(ctx, M_Product_Category_ID, trxName);
	}

	public ProductCategoryInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
