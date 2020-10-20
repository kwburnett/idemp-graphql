package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.respository.PaymentRepository;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class PaymentDataLoader {

	public static String PAYMENT_DATA_LOADER_NAME = "paymentDataLoader";
	private static final PaymentRepository paymentRepository;

	static {
		paymentRepository = new PaymentRepository();
	}

	public static MappedBatchLoader<Integer, List<MPayment_BH>> getBatchLoader() {
		return paymentRepository::getByOrderIds;
	}
}
