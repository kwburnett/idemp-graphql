package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.UserRepository;

public class UserDataLoader extends BaseDataLoader<MUser_BH, MUser_BH, UserRepository> implements DataLoaderRegisterer {
	public static final String USER_BY_ID_DATA_LOADER = "userByIdDataLoader";
	public static final String USER_BY_UUID_DATA_LOADER = "userByUuidDataLoader";
	private final UserRepository userRepository;

	public UserDataLoader() {
		userRepository = new UserRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return USER_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return USER_BY_UUID_DATA_LOADER;
	}

	@Override
	protected UserRepository getRepositoryInstance() {
		return userRepository;
	}
}
