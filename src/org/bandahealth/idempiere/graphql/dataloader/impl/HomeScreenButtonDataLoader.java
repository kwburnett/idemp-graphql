package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MHomeScreenButton;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.HomeScreenButtonRepository;

public class HomeScreenButtonDataLoader
		extends BaseDataLoader<MHomeScreenButton, MHomeScreenButton, HomeScreenButtonRepository>
		implements DataLoaderRegisterer {
	public static final String HOME_SCREEN_BUTTON_DATA_LOADER = "homeScreenButtonDataLoader";
	public static final String HOME_SCREEN_BUTTON_BY_UUID_DATA_LOADER = "homeScreenButtonByUuidDataLoader";
	private final HomeScreenButtonRepository homeScreenButtonRepository;

	public HomeScreenButtonDataLoader() {
		homeScreenButtonRepository = new HomeScreenButtonRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return HOME_SCREEN_BUTTON_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return HOME_SCREEN_BUTTON_BY_UUID_DATA_LOADER;
	}

	@Override
	protected HomeScreenButtonRepository getRepositoryInstance() {
		return homeScreenButtonRepository;
	}
}
