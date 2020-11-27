package org.bandahealth.idempiere.graphql.resolver.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Handle all mutations relating to payments
 */
public class PaymentMutation implements GraphQLMutationResolver {
	private final PaymentRepository paymentRepository;

	public PaymentMutation() {
		paymentRepository = new PaymentRepository();
	}

	/**
	 * Save information specific to a service debt payment, which is a payment
	 *
	 * @param payment       The information to save
	 * @param shouldProcess Whether the payment should be processed after save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated payment
	 */
	public CompletableFuture<MPayment_BH> saveServiceDebtPayment(PaymentInput payment, Boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MPayment_BH savedPayment = paymentRepository.save(payment, idempiereContext);
		if (shouldProcess) {
			return paymentRepository.process(savedPayment.getC_Payment_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedPayment);
	}

	/**
	 * Process the payment without need to update beforehand
	 *
	 * @param id          The UUID of the payment to process
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated payment
	 */
	public CompletableFuture<MPayment_BH> processPayment(String id, DataFetchingEnvironment environment) {
		return paymentRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}
}
