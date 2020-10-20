package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.respository.OrderLineRepository;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class ReferenceListDataLoader {

	public static String PATIENT_TYPE_DATA_LOADER_NAME = "referenceListPatientTypeDataLoader";
	public static String REFERRAL_DATA_LOADER_NAME = "referenceListReferralDataLoader";
	public static String ORDER_PAYMENT_TYPE_DATA_LOADER_NAME = "referenceListOrderPaymentTypeDataLoader";
	public static String INVOICE_PAYMENT_TYPE_DATA_LOADER_NAME = "referenceListInvoicePaymentTypeDataLoader";
	private static final ReferenceListRepository referenceListRepository;

	static {
		referenceListRepository = new ReferenceListRepository();
	}

	public static MappedBatchLoader<String, MRefList> getPatientTypeBatchLoader() {
		return referenceListRepository::getPatientType;
	}

	public static MappedBatchLoader<String, MRefList> getReferralBatchLoader() {
		return referenceListRepository::getReferral;
	}

	public static MappedBatchLoader<String, MRefList> getOrderPaymentTypeBatchLoader() {
		return referenceListRepository::getOrderPaymentType;
	}

	public static MappedBatchLoader<String, MRefList> getInvoicePaymentTypeBatchLoader() {
		return referenceListRepository::getInvoicePaymentType;
	}
}
