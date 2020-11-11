package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class PaymentMutation implements GraphQLMutationResolver {
	private final PaymentRepository paymentRepository;

	public PaymentMutation() {
		paymentRepository = new PaymentRepository();
	}

	public CompletableFuture<MPayment_BH> saveServiceDebtPayment(PaymentInput payment, Boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MPayment_BH savedPayment = paymentRepository.save(payment, idempiereContext);
		if (shouldProcess) {
			return paymentRepository.process(savedPayment.getC_Payment_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedPayment);
	}

	public CompletableFuture<MPayment_BH> processPayment(String id, DataFetchingEnvironment environment) {
		return paymentRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}
}
