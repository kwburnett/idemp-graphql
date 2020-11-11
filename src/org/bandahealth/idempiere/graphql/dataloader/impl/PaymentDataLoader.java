package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.repository.PaymentRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class PaymentDataLoader extends BaseDataLoader<MPayment_BH, PaymentInput, PaymentRepository>
		implements DataLoaderRegisterer {
	public static String PAYMENT_BY_ID_DATA_LOADER = "paymentByIdDataLoader";
	public static String PAYMENT_BY_UUID_DATA_LOADER = "paymentByUuidDataLoader";
	public static String PAYMENT_BY_ORDER_DATA_LOADER = "paymentByOrderDataLoader";
	public static String PAYMENT_BY_BUSINESS_PARTNER_DATA_LOADER = "paymentByBusinessPartnerDataLoader";
	private final PaymentRepository paymentRepository;

	public PaymentDataLoader() {
		paymentRepository = new PaymentRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return PAYMENT_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return PAYMENT_BY_UUID_DATA_LOADER;
	}

	@Override
	protected PaymentRepository getRepositoryInstance() {
		return paymentRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(PAYMENT_BY_ORDER_DATA_LOADER, DataLoader.newMappedDataLoader(getBatchLoader(),
				getOptionsWithCache(idempiereContext)));
		registry.register(PAYMENT_BY_BUSINESS_PARTNER_DATA_LOADER, DataLoader
				.newMappedDataLoader(getByBusinessPartnerBatchLoader(), getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MPayment_BH>> getBatchLoader() {
		return (keys, batchLoaderEnvironment) -> paymentRepository.getByOrderIds(keys, batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, List<MPayment_BH>> getByBusinessPartnerBatchLoader() {
		return (keys, batchLoaderEnvironment) -> paymentRepository.getGroupsByIdsCompletableFuture(
				MPayment_BH::getC_BPartner_ID, MPayment_BH.COLUMNNAME_C_BPartner_ID, keys, batchLoaderEnvironment.getContext());
	}
}
