package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MProduct_BH;

import java.math.BigDecimal;

public class ProductResolver extends BaseResolver<MProduct_BH> implements GraphQLResolver<MProduct_BH> {

	public int reorderLevel(MProduct_BH entity) {
		return entity.getbh_reorder_level();
	}

	public int reorderQuantity(MProduct_BH entity) {
		return entity.getbh_reorder_quantity();
	}

	public BigDecimal buyPrice(MProduct_BH entity) {
		return entity.getBH_BuyPrice();
	}

	public BigDecimal sellPrice(MProduct_BH entity) {
		return entity.getBH_SellPrice();
	}

	public String type(MProduct_BH entity) {
		return entity.getProductType();
	}

	public boolean hasExpiration(MProduct_BH entity) {
		return entity.isBH_HasExpiration();
	}

	public BigDecimal priceMargin(MProduct_BH entity) {
		return entity.getBH_PriceMargin();
	}

	public int productCategoryId(MProduct_BH entity) {
		return entity.getM_Product_Category_ID();
	}

	// TODO: Need to calculate this from inventory
	public BigDecimal totalQuantity(MProduct_BH entity) {
//		return entity.getBH_PriceMargin();
		return BigDecimal.ZERO;
	}
}
