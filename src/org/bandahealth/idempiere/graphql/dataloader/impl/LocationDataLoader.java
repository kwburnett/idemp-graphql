package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.repository.LocationRepository;
import org.compiere.model.MLocation;

public class LocationDataLoader extends BaseDataLoader<MLocation, LocationInput, LocationRepository>
		implements DataLoaderRegisterer {
	public static final String LOCATION_DATA_LOADER = "locationDataLoader";
	public static final String LOCATION_BY_UUID_DATA_LOADER = "locationByUuidDataLoader";
	private final LocationRepository locationRepository;

	public LocationDataLoader() {
		locationRepository = new LocationRepository();
	}

	@Override
	protected String getDefaultByIdDataLoaderName() {
		return LOCATION_DATA_LOADER;
	}

	@Override
	protected String getDefaultByUuidDataLoaderName() {
		return LOCATION_BY_UUID_DATA_LOADER;
	}

	@Override
	protected LocationRepository getRepositoryInstance() {
		return locationRepository;
	}
}
