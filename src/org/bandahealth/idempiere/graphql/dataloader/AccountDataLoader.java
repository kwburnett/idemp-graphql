package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.respository.AccountRepository;
import org.compiere.model.MElementValue;

public class AccountDataLoader extends BaseDataLoader<MElementValue, AccountRepository>
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
