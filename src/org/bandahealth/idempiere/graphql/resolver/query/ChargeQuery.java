package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.ChargeRepository;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class ChargeQuery implements GraphQLQueryResolver {
	private final ChargeRepository chargeRepository;

	public ChargeQuery() {
		chargeRepository = new ChargeRepository();
	}

	public CompletableFuture<MCharge_BH> charge(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MCharge_BH> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ChargeDataLoader.CHARGE_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public Connection<MCharge_BH> expenseCategories(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return chargeRepository.getExpenseCategories(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
