package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;

import java.util.concurrent.CompletableFuture;

public class PaymentMutation implements GraphQLMutationResolver {
	private final PaymentRepository paymentRepository;

	public PaymentMutation() {
		paymentRepository = new PaymentRepository();
	}

	public CompletableFuture<MPayment_BH> saveServiceDebtPayment(PaymentInput payment, Boolean shouldProcess) {
		MPayment_BH savedPayment = paymentRepository.save(payment);
		if (shouldProcess) {
			return paymentRepository.process(savedPayment.getC_Payment_UU());
		}
		return CompletableFuture.supplyAsync(() -> savedPayment);
	}

	public CompletableFuture<MPayment_BH> processPayment(String id) {
		return paymentRepository.process(id);
	}
}
