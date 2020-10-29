package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;

public class BusinessPartnerMutation implements GraphQLMutationResolver {
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerMutation() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public MBPartner_BH saveCustomer(BusinessPartnerInput businessPartner) {
		return businessPartnerRepository.saveCustomer(businessPartner);
	}

	public MBPartner_BH saveVendor(BusinessPartnerInput businessPartner) {
		return businessPartnerRepository.saveVendor(businessPartner);
	}
}
