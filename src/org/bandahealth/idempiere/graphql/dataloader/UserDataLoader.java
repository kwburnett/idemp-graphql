package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.respository.UserRepository;
import org.bandahealth.idempiere.graphql.respository.WarehouseRepository;
import org.compiere.model.MWarehouse;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class UserDataLoader extends BaseDataLoader<MUser_BH, UserRepository> implements DataLoaderRegisterer {

	public static final String USER_DATA_LOADER = "userDataLoader";
	private final UserRepository userRepository;

	public UserDataLoader() {
		userRepository = new UserRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return USER_DATA_LOADER;
	}

	@Override
	protected UserRepository getRepositoryInstance() {
		return userRepository;
	}
}
