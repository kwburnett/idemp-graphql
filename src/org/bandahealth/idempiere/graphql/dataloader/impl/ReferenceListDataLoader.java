package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.model.input.ReferenceListInput;
import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class ReferenceListDataLoader extends BaseDataLoader<MRefList, ReferenceListInput, ReferenceListRepository>
		implements DataLoaderRegisterer {

	public static String PATIENT_TYPE_DATA_LOADER = "referenceListPatientTypeDataLoader";
	public static String REFERRAL_DATA_LOADER = "referenceListReferralDataLoader";
	public static String ORDER_PAYMENT_TYPE_DATA_LOADER = "referenceListOrderPaymentTypeDataLoader";
	public static String INVOICE_PAYMENT_TYPE_DATA_LOADER = "referenceListInvoicePaymentTypeDataLoader";
	public static String NHIF_TYPE_DATA_LOADER = "referenceListNhifTypeDataLoader";
	public static String NHIF_RELATIONSHIP_DATA_LOADER = "referenceListNhifRelationshipDataLoader";
	public static String REFERENCE_LIST_BY_REFERENCE_DATA_LOADER = "referenceListByReferenceDataLoader";
	private final ReferenceListRepository referenceListRepository;

	public ReferenceListDataLoader() {
		referenceListRepository = new ReferenceListRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return null;
	}

	@Override
	protected ReferenceListRepository getRepositoryInstance() {
		return null;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(PATIENT_TYPE_DATA_LOADER, DataLoader.newMappedDataLoader(getPatientTypeBatchLoader(),
				getOptionsWithCache()));
		registry.register(REFERRAL_DATA_LOADER, DataLoader.newMappedDataLoader(getReferralBatchLoader(),
				getOptionsWithCache()));
		registry.register(ORDER_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getOrderPaymentTypeBatchLoader(), getOptionsWithCache()));
		registry.register(INVOICE_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getInvoicePaymentTypeBatchLoader(), getOptionsWithCache()));
		registry.register(NHIF_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getNhifTypeBatchLoader(), getOptionsWithCache()));
		registry.register(NHIF_RELATIONSHIP_DATA_LOADER,
				DataLoader.newMappedDataLoader(getNhifRelationshipBatchLoader(), getOptionsWithCache()));
		registry.register(REFERENCE_LIST_BY_REFERENCE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getByReferenceBatchLoader(), getOptionsWithCache()));
	}

	private MappedBatchLoader<Integer, List<MRefList>> getByReferenceBatchLoader() {
		return keys -> referenceListRepository.getGroupsByIdsCompletableFuture(MRefList::getAD_Reference_ID,
				MRefList.COLUMNNAME_AD_Reference_ID, keys);
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

	private MappedBatchLoader<String, MRefList> getNhifTypeBatchLoader() {
		return referenceListRepository::getNhifType;
	}

	private MappedBatchLoader<String, MRefList> getNhifRelationshipBatchLoader() {
		return referenceListRepository::getNhifRelationship;
	}
}
