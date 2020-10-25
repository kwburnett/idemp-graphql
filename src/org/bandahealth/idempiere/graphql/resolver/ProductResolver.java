package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.dataloader.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.ProductCategoryDataLoader;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

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

	public CompletableFuture<MProductCategory_BH> productCategory(MProduct_BH entity,
																																DataFetchingEnvironment environment) {
		final DataLoader<Integer, MProductCategory_BH> productCategoryDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ProductCategoryDataLoader.PRODUCT_CATEGORY_DATA_LOADER);
		return productCategoryDataLoader.load(entity.getM_Product_Category_ID());
	}

	// TODO: Need to calculate this from inventory
	public BigDecimal totalQuantity(MProduct_BH entity) {
//		return entity.getBH_PriceMargin();
		return BigDecimal.ZERO;
	}
}
