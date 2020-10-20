package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;

public class BusinessPartnerDataLoader extends BaseDataLoader<MBPartner_BH, BusinessPartnerRepository>
		implements DataLoaderRegisterer {

	public static String BUSINESS_PARTNER_DATA_LOADER = "businessPartnerDataLoader";
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerDataLoader() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return BUSINESS_PARTNER_DATA_LOADER;
	}

	@Override
	protected BusinessPartnerRepository getRepositoryInstance() {
		return businessPartnerRepository;
	}
}
