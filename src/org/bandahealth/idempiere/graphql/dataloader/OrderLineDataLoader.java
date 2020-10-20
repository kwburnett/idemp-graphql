package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.respository.OrderLineRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class OrderLineDataLoader implements DataLoaderRegisterer {

	public static String ORDER_LINE_DATA_LOADER_NAME = "orderLineDataLoader";
	private final OrderLineRepository orderLineRepository;

	public OrderLineDataLoader() {
		orderLineRepository = new OrderLineRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(ORDER_LINE_DATA_LOADER_NAME, DataLoader.newMappedDataLoader(getBatchLoader()));
	}

	private MappedBatchLoader<Integer, List<MOrderLine_BH>> getBatchLoader() {
		return orderLineRepository::getByOrderIds;
	}
}
