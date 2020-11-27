package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;

/**
 * Handle all mutations relating to business partners
 */
public class BusinessPartnerMutation implements GraphQLMutationResolver {
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerMutation() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	/**
	 * Save information specific to a customer, which is a business partner
	 *
	 * @param businessPartner The information to save
	 * @param environment     The environment associated with all calls, containing context
	 * @return The updated customer
	 */
	public MBPartner_BH saveCustomer(BusinessPartnerInput businessPartner, DataFetchingEnvironment environment) {
		return businessPartnerRepository.saveCustomer(businessPartner, BandaGraphQLContext.getCtx(environment));
	}

	/**
	 * Save information specific to a vendor, which is a business partner
	 *
	 * @param businessPartner The information to save
	 * @param environment     The environment associated with all calls, containing context
	 * @return The updated vendor
	 */
	public MBPartner_BH saveVendor(BusinessPartnerInput businessPartner, DataFetchingEnvironment environment) {
		return businessPartnerRepository.saveVendor(businessPartner, BandaGraphQLContext.getCtx(environment));
	}
}
