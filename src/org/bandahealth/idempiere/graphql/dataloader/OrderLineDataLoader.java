package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.repository.OrderLineRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class OrderLineDataLoader extends BaseDataLoader<MOrderLine_BH, OrderLineRepository>
		implements DataLoaderRegisterer {

	public static String ORDER_LINE_DATA_LOADER = "orderLineDataLoader";
	public static String ORDER_LINE_BY_ORDER_DATA_LOADER = "orderLineByOrderDataLoader";
	private final OrderLineRepository orderLineRepository;

	public OrderLineDataLoader() {
		orderLineRepository = new OrderLineRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ORDER_LINE_DATA_LOADER;
	}

	@Override
	protected OrderLineRepository getRepositoryInstance() {
		return orderLineRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(ORDER_LINE_BY_ORDER_DATA_LOADER, DataLoader.newMappedDataLoader(getBatchLoader()));
	}

	private MappedBatchLoader<Integer, List<MOrderLine_BH>> getBatchLoader() {
		return keys -> orderLineRepository.getGroupsByIds(MOrderLine_BH::getC_Order_ID, MOrderLine_BH.COLUMNNAME_C_Order_ID,
				keys);
	}
}
