package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;

public class BusinessPartnerMutation implements GraphQLMutationResolver {
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerMutation() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public MBPartner_BH saveCustomer(BusinessPartnerInput businessPartner, DataFetchingEnvironment environment) {
		return businessPartnerRepository.saveCustomer(businessPartner, BandaGraphQLContext.getCtx(environment));
	}

	public MBPartner_BH saveVendor(BusinessPartnerInput businessPartner, DataFetchingEnvironment environment) {
		return businessPartnerRepository.saveVendor(businessPartner, BandaGraphQLContext.getCtx(environment));
	}
}
