package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MDashboardButtonGroupButton;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.DashboardButtonGroupButtonRepository;

public class DashboardButtonGroupButtonDataLoader
		extends BaseDataLoader<MDashboardButtonGroupButton, MDashboardButtonGroupButton,
		DashboardButtonGroupButtonRepository>
		implements DataLoaderRegisterer {
	public static final String DASHBOARD_BUTTON_GROUP_BUTTON_BY_ID_DATA_LOADER =
			"dashboardButtonGroupButtonByIdDataLoader";
	public static final String DASHBOARD_BUTTON_GROUP_BUTTON_BY_UUID_DATA_LOADER =
			"dashboardButtonGroupButtonByUuidDataLoader";
	private final DashboardButtonGroupButtonRepository dashboardButtonGroupButtonRepository;

	public DashboardButtonGroupButtonDataLoader() {
		dashboardButtonGroupButtonRepository = new DashboardButtonGroupButtonRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return DASHBOARD_BUTTON_GROUP_BUTTON_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return DASHBOARD_BUTTON_GROUP_BUTTON_BY_UUID_DATA_LOADER;
	}

	@Override
	protected DashboardButtonGroupButtonRepository getRepositoryInstance() {
		return dashboardButtonGroupButtonRepository;
	}
}
