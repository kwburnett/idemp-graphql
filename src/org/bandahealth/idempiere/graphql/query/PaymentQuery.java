package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;

public class PaymentQuery implements GraphQLQueryResolver {
	private final PaymentRepository paymentRepository;

	public PaymentQuery() {
		paymentRepository = new PaymentRepository();
	}

	public Connection<MPayment_BH> serviceDebtPayments(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return paymentRepository.getServiceDebtPayments(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
