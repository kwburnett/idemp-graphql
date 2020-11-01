package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.repository.LocationRepository;
import org.compiere.model.MLocation;

public class LocationDataLoader extends BaseDataLoader<MLocation, LocationInput, LocationRepository>
		implements DataLoaderRegisterer {
	public static final String LOCATION_BY_ID_DATA_LOADER = "locationByIdDataLoader";
	public static final String LOCATION_BY_UUID_DATA_LOADER = "locationByUuidDataLoader";
	private final LocationRepository locationRepository;

	public LocationDataLoader() {
		locationRepository = new LocationRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return LOCATION_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return LOCATION_BY_UUID_DATA_LOADER;
	}

	@Override
	protected LocationRepository getRepositoryInstance() {
		return locationRepository;
	}
}
