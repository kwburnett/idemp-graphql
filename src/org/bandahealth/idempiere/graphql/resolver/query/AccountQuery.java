package org.bandahealth.idempiere.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.dataloader.impl.AccountDataLoader;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.AccountRepository;
import org.compiere.model.MElementValue;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class AccountQuery implements GraphQLQueryResolver {
	private final AccountRepository accountRepository;

	public AccountQuery() {
		accountRepository = new AccountRepository();
	}

	public CompletableFuture<MElementValue> account(String id, DataFetchingEnvironment environment) {
		final DataLoader<String, MElementValue> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(AccountDataLoader.ACCOUNT_BY_UUID_DATA_LOADER);
		return dataLoader.load(id);
	}

	public Connection<MElementValue> accounts(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return accountRepository.get(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
