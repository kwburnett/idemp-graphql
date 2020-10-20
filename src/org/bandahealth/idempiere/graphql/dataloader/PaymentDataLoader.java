package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.respository.PaymentRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class PaymentDataLoader implements DataLoaderRegisterer {

	public static String PAYMENT_DATA_LOADER_NAME = "paymentDataLoader";
	private final PaymentRepository paymentRepository;

	public PaymentDataLoader() {
		paymentRepository = new PaymentRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(PAYMENT_DATA_LOADER_NAME, DataLoader.newMappedDataLoader(getBatchLoader()));
	}

	private MappedBatchLoader<Integer, List<MPayment_BH>> getBatchLoader() {
		return paymentRepository::getByOrderIds;
	}
}
