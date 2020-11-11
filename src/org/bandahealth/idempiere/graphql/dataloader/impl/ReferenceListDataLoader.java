package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.ReferenceListInput;
import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class ReferenceListDataLoader extends BaseDataLoader<MRefList, ReferenceListInput, ReferenceListRepository>
		implements DataLoaderRegisterer {
	public static String PATIENT_TYPE_DATA_LOADER = "referenceListPatientTypeDataLoader";
	public static String REFERRAL_DATA_LOADER = "referenceListReferralDataLoader";
	public static String ORDER_PAYMENT_TYPE_DATA_LOADER = "referenceListOrderPaymentTypeDataLoader";
	public static String INVOICE_PAYMENT_TYPE_DATA_LOADER = "referenceListInvoicePaymentTypeDataLoader";
	public static String NHIF_TYPE_DATA_LOADER = "referenceListNhifTypeDataLoader";
	public static String NHIF_RELATIONSHIP_DATA_LOADER = "referenceListNhifRelationshipDataLoader";
	public static String REFERENCE_LIST_BY_REFERENCE_DATA_LOADER = "referenceListByReferenceDataLoader";
	public static String REFERENCE_LIST_BY_PRODUCT_CATEGORY_TYPE_DATA_LOADER =
			"referenceListByProductCategoryTypeDataLoader";
	public static String REFERENCE_LIST_BY_DOCUMENT_STATUS_DATA_LOADER = "referenceListByDocumentStatusDataLoader";
	private final ReferenceListRepository referenceListRepository;

	public ReferenceListDataLoader() {
		referenceListRepository = new ReferenceListRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return null;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return null;
	}

	@Override
	protected ReferenceListRepository getRepositoryInstance() {
		return null;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		// Don't use the cache in any of these since we fetch value by strings that aren't unique
		registry.register(PATIENT_TYPE_DATA_LOADER, DataLoader.newMappedDataLoader(getPatientTypeBatchLoader(),
				getOptionsWithoutCache(idempiereContext)));
		registry.register(REFERRAL_DATA_LOADER, DataLoader.newMappedDataLoader(getReferralBatchLoader(),
				getOptionsWithoutCache(idempiereContext)));
		registry.register(ORDER_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getOrderPaymentTypeBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(INVOICE_PAYMENT_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getInvoicePaymentTypeBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(NHIF_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getNhifTypeBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(NHIF_RELATIONSHIP_DATA_LOADER,
				DataLoader.newMappedDataLoader(getNhifRelationshipBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(REFERENCE_LIST_BY_REFERENCE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getByReferenceBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(REFERENCE_LIST_BY_PRODUCT_CATEGORY_TYPE_DATA_LOADER,
				DataLoader.newMappedDataLoader(getProductCategoryTypeBatchLoader(), getOptionsWithoutCache(idempiereContext)));
		registry.register(REFERENCE_LIST_BY_DOCUMENT_STATUS_DATA_LOADER,
				DataLoader.newMappedDataLoader(getDocumentStatusTypeBatchLoader(), getOptionsWithoutCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MRefList>> getByReferenceBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getGroupsByIdsCompletableFuture(
				MRefList::getAD_Reference_ID, MRefList.COLUMNNAME_AD_Reference_ID, keys, batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getPatientTypeBatchLoader() {
		return (keys, bath) -> referenceListRepository.getPatientType(keys, bath.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getReferralBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getReferral(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getOrderPaymentTypeBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getOrderPaymentType(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getInvoicePaymentTypeBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getInvoicePaymentType(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getNhifTypeBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getNhifType(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getNhifRelationshipBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getNhifRelationship(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getProductCategoryTypeBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getProductCategoryType(keys,
				batchLoaderEnvironment.getContext());
	}

	private MappedBatchLoaderWithContext<String, MRefList> getDocumentStatusTypeBatchLoader() {
		return (keys, batchLoaderEnvironment) -> referenceListRepository.getDocumentStatus(keys,
				batchLoaderEnvironment.getContext());
	}
}
