package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderQuery implements GraphQLQueryResolver {

	private final OrderRepository orderRepository;

	public OrderQuery() {
		orderRepository = new OrderRepository();
	}

	public CompletableFuture<MOrder_BH> order(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MOrder_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(OrderDataLoader.ORDER_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public CompletableFuture<Connection<MOrder_BH>> salesOrders(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return CompletableFuture.supplyAsync(() ->
				orderRepository.getSalesOrders(filter, sort, new PagingInfo(page, pageSize), environment));
	}

	public CompletableFuture<Connection<MOrder_BH>> purchaseOrders(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return CompletableFuture.supplyAsync(() ->
				orderRepository.getPurchaseOrders(filter, sort, new PagingInfo(page, pageSize), environment));
	}
}
