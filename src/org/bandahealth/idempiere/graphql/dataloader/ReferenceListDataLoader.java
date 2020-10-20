package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public class ReferenceListDataLoader implements DataLoaderRegisterer {

	public static String PATIENT_TYPE_DATA_LOADER = "referenceListPatientTypeDataLoader";
	public static String REFERRAL_DATA_LOADER = "referenceListReferralDataLoader";
	public static String ORDER_PAYMENT_TYPE_DATA_LOADER = "referenceListOrderPaymentTypeDataLoader";
	public static String INVOICE_PAYMENT_TYPE_DATA_LOADER = "referenceListInvoicePaymentTypeDataLoader";
	private final ReferenceListRepository referenceListRepository;

	public ReferenceListDataLoader() {
		referenceListRepository = new ReferenceListRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(PATIENT_TYPE_DATA_LOADER, DataLoader.newMappedDataLoader(getPatientTypeBatchLoader()));
		registry.register(REFERRAL_DATA_LOADER, DataLoader.newMappedDataLoader(getReferralBatchLoader()));
		registry.register(ORDER_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getOrderPaymentTypeBatchLoader()));
		registry.register(INVOICE_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getInvoicePaymentTypeBatchLoader()));
	}

	private MappedBatchLoader<String, MRefList> getPatientTypeBatchLoader() {
		return referenceListRepository::getPatientType;
	}

	private MappedBatchLoader<String, MRefList> getReferralBatchLoader() {
		return referenceListRepository::getReferral;
	}

	private MappedBatchLoader<String, MRefList> getOrderPaymentTypeBatchLoader() {
		return referenceListRepository::getOrderPaymentType;
	}

	private MappedBatchLoader<String, MRefList> getInvoicePaymentTypeBatchLoader() {
		return referenceListRepository::getInvoicePaymentType;
	}
}
