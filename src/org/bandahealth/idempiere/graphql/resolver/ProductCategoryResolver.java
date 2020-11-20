package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProductCategory_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

/**
 * The Product Category resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class ProductCategoryResolver extends BaseResolver<MProductCategory_BH>
		implements GraphQLResolver<MProductCategory_BH> {

	public CompletableFuture<MRefList> productCategoryType(MProductCategory_BH entity,
			DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ReferenceListDataLoader.REFERENCE_LIST_BY_PRODUCT_CATEGORY_TYPE_DATA_LOADER);
		return dataLoader.load(entity.getBH_Product_Category_Type());
	}
}
