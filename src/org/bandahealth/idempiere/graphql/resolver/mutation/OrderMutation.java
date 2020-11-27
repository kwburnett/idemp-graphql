package org.bandahealth.idempiere.graphql.resolver.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.OrderRepository;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Handle all mutations relating to orders
 */
public class OrderMutation implements GraphQLMutationResolver {

	private final OrderRepository orderRepository;

	public OrderMutation() {
		orderRepository = new OrderRepository();
	}

	/**
	 * Save information specific to a sales order, which is an order
	 *
	 * @param order         The information to save
	 * @param shouldProcess Whether the order should be processed after save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated order
	 */
	public CompletableFuture<MOrder_BH> saveSalesOrder(OrderInput order, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MOrder_BH savedOrder = orderRepository.saveSalesOrder(order, idempiereContext);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	/**
	 * Save information specific to a purchase order, which is an order
	 *
	 * @param order         The information to save
	 * @param shouldProcess Whether the order should be processed after save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated order
	 */
	public CompletableFuture<MOrder_BH> savePurchaseOrder(OrderInput order, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MOrder_BH savedOrder = orderRepository.savePurchaseOrder(order, idempiereContext);
		if (shouldProcess) {
			return orderRepository.process(savedOrder.getC_Order_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	/**
	 * Process the order without need to update beforehand
	 *
	 * @param id          The UUID of the order to process
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated invoice
	 */
	public CompletableFuture<MOrder_BH> processOrder(String id, DataFetchingEnvironment environment) {
		return orderRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}

	/**
	 * Delete the specified order
	 *
	 * @param id          The UUID of the invoice to process
	 * @param environment The environment associated with all calls, containing context
	 * @return Whether the order was successfully deleted or not
	 */
	public CompletableFuture<Boolean> deleteOrder(String id, DataFetchingEnvironment environment) {
		return orderRepository.delete(id, BandaGraphQLContext.getCtx(environment));
	}
}
