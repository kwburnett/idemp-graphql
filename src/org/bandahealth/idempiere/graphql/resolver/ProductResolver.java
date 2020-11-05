package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProductCategoryDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.StorageOnHandDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MStorageOnHand;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.util.List;
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
		final DataLoader<String, MProductCategory_BH> productCategoryDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ProductCategoryDataLoader.PRODUCT_CATEGORY_BY_ID_DATA_LOADER);
		return productCategoryDataLoader.load(ModelUtil.getModelKey(entity, entity.getM_Product_Category_ID()));
	}

	public CompletableFuture<BigDecimal> totalQuantity(MProduct_BH entity, DataFetchingEnvironment environment) {
		return storageOnHand(entity, environment).thenApply(storageOnHandLines ->
				storageOnHandLines == null ? BigDecimal.ZERO : storageOnHandLines.stream().map(MStorageOnHand::getQtyOnHand)
						.reduce(BigDecimal.ZERO, BigDecimal::add)
		);
	}

	public CompletableFuture<List<MStorageOnHand>> storageOnHand(MProduct_BH entity,
			DataFetchingEnvironment environment) {
		final DataLoader<String, List<MStorageOnHand>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(StorageOnHandDataLoader.STORAGE_ON_HAND_BY_PRODUCT_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.get_ID()));
	}
}
