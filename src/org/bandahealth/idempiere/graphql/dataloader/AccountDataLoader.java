package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.model.input.AccountInput;
import org.bandahealth.idempiere.graphql.repository.AccountRepository;
import org.compiere.model.MElementValue;

public class AccountDataLoader extends BaseDataLoader<MElementValue, AccountInput, AccountRepository>
		implements DataLoaderRegisterer {

	public static final String ACCOUNT_DATA_LOADER = "accountDataLoader";
	private final AccountRepository accountRepository;

	public AccountDataLoader() {
		accountRepository = new AccountRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return ACCOUNT_DATA_LOADER;
	}

	@Override
	protected AccountRepository getRepositoryInstance() {
		return accountRepository;
	}
}
