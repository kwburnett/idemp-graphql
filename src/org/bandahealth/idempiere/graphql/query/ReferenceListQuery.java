package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.repository.ReferenceListRepository;
import org.compiere.model.MRefList;

import java.util.List;

public class ReferenceListQuery implements GraphQLQueryResolver {
	private final ReferenceListRepository referenceListRepository;

	public ReferenceListQuery() {
		referenceListRepository = new ReferenceListRepository();
	}

	public List<MRefList> patientTypes(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.PATIENT_TYPE,
				BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> orderPaymentTypes(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.ORDER_PAYMENT_TYPE,
				BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> invoicePaymentTypes(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.INVOICE_PAYMENT_TYPE,
				BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> nhifTypes(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.NHIF_TYPE, BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> nhifRelationships(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.NHIF_RELATIONSHIP,
				BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> referrals(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.REFERRAL_DROPDOWN,
				BandaGraphQLContext.getCtx(environment));
	}

	public List<MRefList> productCategoryTypes(DataFetchingEnvironment environment) {
		return referenceListRepository.getTypes(ReferenceListRepository.PRODUCT_CATEGORY_TYPE,
				BandaGraphQLContext.getCtx(environment));
	}
}
