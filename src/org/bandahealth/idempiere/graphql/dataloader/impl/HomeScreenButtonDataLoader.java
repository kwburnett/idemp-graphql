package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MHomeScreenButton;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.HomeScreenButtonRepository;

public class HomeScreenButtonDataLoader
		extends BaseDataLoader<MHomeScreenButton, MHomeScreenButton, HomeScreenButtonRepository>
		implements DataLoaderRegisterer {
	public static final String HOME_SCREEN_BUTTON_DATA_LOADER = "homeScreenButtonDataLoader";
	private final HomeScreenButtonRepository homeScreenButtonRepository;

	public HomeScreenButtonDataLoader() {
		homeScreenButtonRepository = new HomeScreenButtonRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return HOME_SCREEN_BUTTON_DATA_LOADER;
	}

	@Override
	protected HomeScreenButtonRepository getRepositoryInstance() {
		return homeScreenButtonRepository;
	}
}
