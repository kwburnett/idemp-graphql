package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.respository.OrderRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public class OrderDataLoader implements DataLoaderRegisterer {

	public static String SALES_ORDER_COUNT_DATA_LOADER_NAME = "salesOrderCountDataLoader";
	private final OrderRepository orderRepository;

	public OrderDataLoader() {
		orderRepository = new OrderRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(SALES_ORDER_COUNT_DATA_LOADER_NAME,
				DataLoader.newMappedDataLoader(getSalesOrderCountBatchLoader()));
	}

	private MappedBatchLoader<Integer, Integer> getSalesOrderCountBatchLoader() {
		return orderRepository::getSalesOrderCountsByBusinessPartner;
	}
}
