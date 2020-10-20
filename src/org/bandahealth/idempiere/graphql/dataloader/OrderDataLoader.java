package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.respository.OrderRepository;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.MappedBatchLoader;

public class OrderDataLoader {

	public static String SALES_ORDER_COUNT_DATA_LOADER_NAME = "salesOrderCountDataLoader";
	private static final OrderRepository orderRepository;

	static {
		orderRepository = new OrderRepository();
	}

	public static MappedBatchLoader<Integer, Integer> getSalesOrderCountBatchLoader() {
		return orderRepository::getSalesOrderCountsByBusinessPartner;
	}
}
