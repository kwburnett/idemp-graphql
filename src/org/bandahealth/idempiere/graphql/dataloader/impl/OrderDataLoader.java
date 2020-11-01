package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class OrderDataLoader extends BaseDataLoader<MOrder_BH, OrderInput, OrderRepository>
		implements DataLoaderRegisterer {
	public static String ORDER_BY_ID_DATA_LOADER = "orderByIdDataLoader";
	public static String ORDER_BY_UUID_DATA_LOADER = "orderByUuidDataLoader";
	public static String SALES_ORDER_BY_BUSINESS_PARTNER_DATA_LOADER = "salesOrderByBusinessPartnerDataLoader";
	private final OrderRepository orderRepository;

	public OrderDataLoader() {
		orderRepository = new OrderRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ORDER_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ORDER_BY_UUID_DATA_LOADER;
	}

	@Override
	protected OrderRepository getRepositoryInstance() {
		return orderRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(SALES_ORDER_BY_BUSINESS_PARTNER_DATA_LOADER,
				DataLoader.newMappedDataLoader(getSalesOrderByBusinessPartnerBatchLoader(), getOptionsWithCache()));
	}

	private MappedBatchLoader<Integer, List<MOrder_BH>> getSalesOrderByBusinessPartnerBatchLoader() {
		return keys -> orderRepository.getGroupsByIdsCompletableFuture(MOrder_BH::getC_BPartner_ID, MOrder_BH.COLUMNNAME_C_BPartner_ID,
				keys);
	}
}
