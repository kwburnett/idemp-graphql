package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public class OrderDataLoader extends BaseDataLoader<MOrder_BH, OrderRepository> implements DataLoaderRegisterer {

	public static String SALES_ORDER_COUNT_DATA_LOADER = "salesOrderCountDataLoader";
	public static String ORDER_DATA_LOADER = "orderDataLoader";
	private final OrderRepository orderRepository;

	public OrderDataLoader() {
		orderRepository = new OrderRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ORDER_DATA_LOADER;
	}

	@Override
	protected OrderRepository getRepositoryInstance() {
		return orderRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(SALES_ORDER_COUNT_DATA_LOADER,
				DataLoader.newMappedDataLoader(getSalesOrderCountBatchLoader()));
	}

	private MappedBatchLoader<Integer, Integer> getSalesOrderCountBatchLoader() {
		return orderRepository::getSalesOrderCountsByBusinessPartner;
	}
}
