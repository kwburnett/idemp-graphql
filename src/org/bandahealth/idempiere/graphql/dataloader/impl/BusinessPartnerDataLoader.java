package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.BusinessPartnerInput;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;

public class BusinessPartnerDataLoader
		extends BaseDataLoader<MBPartner_BH, BusinessPartnerInput, BusinessPartnerRepository>
		implements DataLoaderRegisterer {

	public static String BUSINESS_PARTNER_BY_ID_DATA_LOADER = "businessPartnerByIdDataLoader";
	public static String BUSINESS_PARTNER_BY_UUID_DATA_LOADER = "businessPartnerByUuidDataLoader";
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerDataLoader() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return BUSINESS_PARTNER_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return BUSINESS_PARTNER_BY_UUID_DATA_LOADER;
	}

	@Override
	protected BusinessPartnerRepository getRepositoryInstance() {
		return businessPartnerRepository;
	}
}
