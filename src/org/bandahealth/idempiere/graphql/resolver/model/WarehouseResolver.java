package org.bandahealth.idempiere.graphql.resolver.model;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocatorDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MLocator;
import org.compiere.model.MWarehouse;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Warehouse resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class WarehouseResolver extends BaseResolver<MWarehouse> implements GraphQLResolver<MWarehouse> {

	public CompletableFuture<List<MLocator>> locators(MWarehouse entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MLocator>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(LocatorDataLoader.LOCATOR_BY_WAREHOUSE_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getM_Warehouse_ID()));
	}
}
