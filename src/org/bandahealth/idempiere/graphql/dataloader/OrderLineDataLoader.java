package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.respository.OrderLineRepository;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class OrderLineDataLoader {

	public static String ORDER_LINE_DATA_LOADER_NAME = "orderLineDataLoader";
	private static final OrderLineRepository orderLineRepository;

	static {
		orderLineRepository = new OrderLineRepository();
	}

	public static MappedBatchLoader<Integer, List<MOrderLine_BH>> getBatchLoader() {
		return orderLineRepository::getByOrderIds;
	}
}
