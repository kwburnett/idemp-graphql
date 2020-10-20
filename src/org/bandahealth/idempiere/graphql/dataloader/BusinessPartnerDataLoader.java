package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.respository.BusinessPartnerRepository;
import org.dataloader.MappedBatchLoader;

public class BusinessPartnerDataLoader {

	public static String BUSINESS_PARTNER_DATA_LOADER_NAME = "businessPartnerDataLoader";
	private static final BusinessPartnerRepository businessPartnerRepository;

	static {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public static MappedBatchLoader<Integer, MBPartner_BH> getBatchLoader() {
		return businessPartnerRepository::getByIds;
	}
}
