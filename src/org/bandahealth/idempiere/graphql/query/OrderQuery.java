package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderQuery implements GraphQLQueryResolver {

	private final OrderRepository orderRepository;

	public OrderQuery() {
		orderRepository = new OrderRepository();
	}

	public CompletableFuture<List<MOrder_BH>> salesOrders(String filter, String sort, int page, int pageSize) {
		return CompletableFuture.supplyAsync(() -> orderRepository.getSalesOrders(filter, sort, page, pageSize));
	}
}
