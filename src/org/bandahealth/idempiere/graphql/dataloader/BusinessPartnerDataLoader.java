package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.respository.BusinessPartnerRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public class BusinessPartnerDataLoader implements DataLoaderRegisterer {

	public static String BUSINESS_PARTNER_DATA_LOADER_NAME = "businessPartnerDataLoader";
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerDataLoader() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public MappedBatchLoader<Integer, MBPartner_BH> getBatchLoader() {
		return businessPartnerRepository::getByIds;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		registry.register(BUSINESS_PARTNER_DATA_LOADER_NAME, DataLoader.newMappedDataLoader(getBatchLoader()));
	}
}
