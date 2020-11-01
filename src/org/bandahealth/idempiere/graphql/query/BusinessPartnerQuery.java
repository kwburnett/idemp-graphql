package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.BusinessPartnerDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.BusinessPartnerRepository;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class BusinessPartnerQuery implements GraphQLQueryResolver {
	private final BusinessPartnerRepository businessPartnerRepository;

	public BusinessPartnerQuery() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public CompletableFuture<MBPartner_BH> businessPartner(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MBPartner_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(BusinessPartnerDataLoader.BUSINESS_PARTNER_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public Connection<MBPartner_BH> customers(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return businessPartnerRepository.getCustomers(filter, sort, new PagingInfo(page, pageSize), environment);
	}

	public Connection<MBPartner_BH> vendors(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return businessPartnerRepository.getVendors(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
