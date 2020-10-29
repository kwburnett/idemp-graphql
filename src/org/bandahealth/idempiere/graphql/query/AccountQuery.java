package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.AccountRepository;
import org.compiere.model.MElementValue;

public class AccountQuery implements GraphQLQueryResolver {
	private final AccountRepository accountRepository;

	public AccountQuery() {
		accountRepository = new AccountRepository();
	}

	public Connection<MElementValue> accounts(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return accountRepository.get(filter, sort, new PagingInfo(page, pageSize), environment);
	}
}
