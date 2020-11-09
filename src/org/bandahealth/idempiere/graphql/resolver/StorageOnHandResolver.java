package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.AttributeSetInstanceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocatorDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProductDataLoader;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MLocator;
import org.compiere.model.MStorageOnHand;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class StorageOnHandResolver extends BaseResolver<MStorageOnHand> implements GraphQLResolver<MStorageOnHand> {

	public CompletableFuture<MLocator> locator(MStorageOnHand entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MLocator> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(LocatorDataLoader.LOCATOR_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_Locator_ID());
	}

	public CompletableFuture<MProduct_BH> product(MStorageOnHand entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MProduct_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ProductDataLoader.PRODUCT_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_Product_ID());
	}

	public CompletableFuture<MAttributeSetInstance> attributeSetInstance(MStorageOnHand entity,
			DataFetchingEnvironment environment) {
		final DataLoader<Integer, MAttributeSetInstance> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(AttributeSetInstanceDataLoader.ATTRIBUTE_SET_INSTANCE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_AttributeSetInstance_ID());
	}

	public BigDecimal quantityOnHand(MStorageOnHand entity) {
		return entity.getQtyOnHand();
	}
}
