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

	public CompletableFuture<MOrder_BH> saveSalesOrder(OrderInput order, boolean shouldProcess) {
		MOrder_BH savedOrder = orderRepository.saveSalesOrder(order);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU());
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MOrder_BH> savePurchaseOrder(OrderInput order, boolean shouldProcess) {
		MOrder_BH savedOrder = orderRepository.savePurchaseOrder(order);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU());
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MOrder_BH> processOrder(String id) {
		return orderRepository.process(id);
	}
}
