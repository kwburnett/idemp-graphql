package org.bandahealth.idempiere.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.PaymentDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class PaymentQuery implements GraphQLQueryResolver {
	private final PaymentRepository paymentRepository;

	public PaymentQuery() {
		paymentRepository = new PaymentRepository();
	}

	public CompletableFuture<MPayment_BH> payment(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MPayment_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(PaymentDataLoader.PAYMENT_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public Connection<MPayment_BH> serviceDebtPayments(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return paymentRepository.getServiceDebtPayments(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
