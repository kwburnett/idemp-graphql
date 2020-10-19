package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.respository.OrderRepository;

import java.util.List;

public class OrderQuery implements GraphQLQueryResolver {

	private final OrderRepository orderRepository;

	public OrderQuery() {
		orderRepository = new OrderRepository();
	}

	public List<MOrder_BH> salesOrders(String filter, String sort, int page, int pageSize) {
		return orderRepository.getSalesOrders(filter, sort, page, pageSize);
	}
}
