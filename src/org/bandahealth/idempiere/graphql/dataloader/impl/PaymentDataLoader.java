package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class PaymentDataLoader extends BaseDataLoader<MPayment_BH, PaymentInput, PaymentRepository>
		implements DataLoaderRegisterer {

	public static String PAYMENT_DATA_LOADER = "paymentDataLoader";
	public static String PAYMENT_BY_ORDER_DATA_LOADER = "paymentByOrderDataLoader";
	public static String PAYMENT_BY_BUSINESS_PARTNER_DATA_LOADER = "paymentByBusinessPartnerDataLoader";
	private final PaymentRepository paymentRepository;

	public PaymentDataLoader() {
		paymentRepository = new PaymentRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return PAYMENT_DATA_LOADER;
	}

	@Override
	protected PaymentRepository getRepositoryInstance() {
		return paymentRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(PAYMENT_BY_ORDER_DATA_LOADER, DataLoader.newMappedDataLoader(getBatchLoader(),
				getOptionsWithCache()));
		registry.register(PAYMENT_BY_BUSINESS_PARTNER_DATA_LOADER, DataLoader
				.newMappedDataLoader(getByBusinessPartnerBatchLoader(), getOptionsWithCache()));
	}

	private MappedBatchLoader<Integer, List<MPayment_BH>> getBatchLoader() {
		return paymentRepository::getByOrderIds;
	}

	private MappedBatchLoader<Integer, List<MPayment_BH>> getByBusinessPartnerBatchLoader() {
		return keys -> paymentRepository.getGroupsByIdsCompletableFuture(MPayment_BH::getC_BPartner_ID,
				MPayment_BH.COLUMNNAME_C_BPartner_ID, keys);
	}
}
