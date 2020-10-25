package org.bandahealth.idempiere.graphql.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.compiere.model.MExpenseType;
import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.X_I_Product;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

@JsonIgnoreProperties({"resource"})
public class ProductInput extends MProduct_BH {

	private ProductCategoryInput productCategory;

	public ProductInput() {
		super(Env.getCtx(), 0, null);
	}

	public ProductInput(Properties ctx, int M_Product_ID, String trxName) {
		super(ctx, M_Product_ID, trxName);
	}

	public ProductInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public ProductInput(MExpenseType et) {
		super(et);
	}

	public ProductInput(MResource resource, MResourceType resourceType) {
		super(resource, resourceType);
	}

	public ProductInput(X_I_Product impP) {
		super(impP);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setProductCategory(ProductCategoryInput productCategory) {
		this.productCategory = productCategory;
	}

	public ProductCategoryInput getProductCategory() {
		return productCategory;
	}

	public void setReorderLevel(int reorderLevel) {
		this.setbh_reorder_level(reorderLevel);
	}

	public void setReorderQuantity(int reorderQuantity) {
		this.setbh_reorder_quantity(reorderQuantity);
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.setBH_BuyPrice(buyPrice);
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.setBH_SellPrice(sellPrice);
	}

	public void setType(String type) {
		this.setProductType(type);
	}

	public void setHasExpiration(boolean hasExpiration) {
		this.setBH_HasExpiration(hasExpiration);
	}

	public void setPriceMargin(BigDecimal priceMargin) {
		this.setBH_PriceMargin(priceMargin);
	}
}
