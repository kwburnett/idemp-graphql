package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;
import org.compiere.util.Env;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class OrderMutation implements GraphQLMutationResolver {

	private final OrderRepository orderRepository;

	public OrderMutation() {
		orderRepository = new OrderRepository();
	}

	public CompletableFuture<MOrder_BH> saveSalesOrder(OrderInput order, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MOrder_BH savedOrder = orderRepository.saveSalesOrder(order, idempiereContext);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MOrder_BH> savePurchaseOrder(OrderInput order, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MOrder_BH savedOrder = orderRepository.savePurchaseOrder(order, idempiereContext);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MOrder_BH> processOrder(String id, DataFetchingEnvironment environment) {
		return orderRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}

	public CompletableFuture<Boolean> deleteOrder(String id, DataFetchingEnvironment environment) {
		return orderRepository.delete(id, BandaGraphQLContext.getCtx(environment));
	}
}
