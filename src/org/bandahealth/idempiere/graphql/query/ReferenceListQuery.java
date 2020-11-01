package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;

import java.util.List;

public class ReferenceListQuery implements GraphQLQueryResolver {
	private final ReferenceListRepository referenceListRepository;

	public ReferenceListQuery() {
		referenceListRepository = new ReferenceListRepository();
	}

	public List<MRefList> patientTypes() {
		return referenceListRepository.getTypes(ReferenceListRepository.PATIENT_TYPE);
	}

	public List<MRefList> orderPaymentTypes() {
		return referenceListRepository.getTypes(ReferenceListRepository.ORDER_PAYMENT_TYPE);
	}

	public List<MRefList> invoicePaymentTypes() {
		return referenceListRepository.getTypes(ReferenceListRepository.INVOICE_PAYMENT_TYPE);
	}

	public List<MRefList> nhifTypes() {
		return referenceListRepository.getTypes(ReferenceListRepository.NHIF_TYPE);
	}

	public List<MRefList> nhifRelationships() {
		return referenceListRepository.getTypes(ReferenceListRepository.NHIF_RELATIONSHIP);
	}

	public List<MRefList> referrals() {
		return referenceListRepository.getTypes(ReferenceListRepository.REFERRAL_DROPDOWN);
	}

	public List<MRefList> productCategoryTypes() {
		return referenceListRepository.getTypes(ReferenceListRepository.PRODUCT_CATEGORY_TYPE);
	}
}
