package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;
import org.compiere.util.Env;

import java.util.concurrent.CompletableFuture;

public class OrderMutation implements GraphQLMutationResolver {

	private final OrderRepository orderRepository;

	public OrderMutation() {
		orderRepository = new OrderRepository();
	}

	public CompletableFuture<MOrder_BH> saveOrder(OrderInput salesOrder, boolean shouldProcess) {
		MOrder_BH order = orderRepository.saveSalesOrder(salesOrder);
		if (shouldProcess) {
			return orderRepository.process(order.getC_Order_UU());
		}
		return CompletableFuture.supplyAsync(() -> order);
	}
}
