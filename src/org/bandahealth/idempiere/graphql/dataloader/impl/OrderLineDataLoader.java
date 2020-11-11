package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.OrderLineInput;
import org.bandahealth.idempiere.graphql.repository.OrderLineRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class OrderLineDataLoader extends BaseDataLoader<MOrderLine_BH, OrderLineInput, OrderLineRepository>
		implements DataLoaderRegisterer {
	public static String ORDER_LINE_BY_ID_DATA_LOADER = "orderLineByIdDataLoader";
	public static String ORDER_LINE_BY_UUID_DATA_LOADER = "orderLineByUuidDataLoader";
	public static String ORDER_LINE_BY_ORDER_DATA_LOADER = "orderLineByOrderDataLoader";
	private final OrderLineRepository orderLineRepository;

	public OrderLineDataLoader() {
		orderLineRepository = new OrderLineRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ORDER_LINE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ORDER_LINE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected OrderLineRepository getRepositoryInstance() {
		return orderLineRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(ORDER_LINE_BY_ORDER_DATA_LOADER, DataLoader.newMappedDataLoader(getBatchLoader(),
				getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MOrderLine_BH>> getBatchLoader() {
		return (keys, batchLoaderEnvironment) -> orderLineRepository.getGroupsByIdsCompletableFuture(
				MOrderLine_BH::getC_Order_ID, MOrderLine_BH.COLUMNNAME_C_Order_ID, keys, batchLoaderEnvironment.getContext());
	}
}
