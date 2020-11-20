package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.WarehouseDataLoader;
import org.compiere.model.MLocator;
import org.compiere.model.MWarehouse;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

/**
 * The Locator resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
public class LocatorResolver extends BaseResolver<MLocator> implements GraphQLResolver<MLocator> {

	public CompletableFuture<MWarehouse> warehouse(MLocator entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MWarehouse> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(WarehouseDataLoader.WAREHOUSE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_Warehouse_ID());
	}
}
