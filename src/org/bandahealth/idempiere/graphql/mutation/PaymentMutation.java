package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;

public class PaymentMutation implements GraphQLMutationResolver {
	private final PaymentRepository paymentRepository;

	public PaymentMutation() {
		paymentRepository = new PaymentRepository();
	}

	public MPayment_BH saveServiceDebtPayment(PaymentInput payment) {
		return paymentRepository.save(payment);
	}
}
