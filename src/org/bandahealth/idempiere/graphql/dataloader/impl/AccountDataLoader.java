package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.AccountInput;
import org.bandahealth.idempiere.graphql.repository.AccountRepository;
import org.compiere.model.MElementValue;

public class AccountDataLoader extends BaseDataLoader<MElementValue, AccountInput, AccountRepository>
		implements DataLoaderRegisterer {

	public static final String ACCOUNT_BY_ID_DATA_LOADER = "accountByIdDataLoader";
	public static final String ACCOUNT_BY_UUID_DATA_LOADER = "accountByUuidDataLoader";
	private final AccountRepository accountRepository;

	public AccountDataLoader() {
		accountRepository = new AccountRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return ACCOUNT_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return ACCOUNT_BY_UUID_DATA_LOADER;
	}

	@Override
	protected AccountRepository getRepositoryInstance() {
		return accountRepository;
	}
}
