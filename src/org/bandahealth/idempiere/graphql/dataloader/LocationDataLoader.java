package org.bandahealth.idempiere.graphql.dataloader;

import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.repository.LocationRepository;
import org.compiere.model.MLocation;

public class LocationDataLoader extends BaseDataLoader<MLocation, LocationInput, LocationRepository>
		implements DataLoaderRegisterer {

	public static final String LOCATION_DATA_LOADER = "locationDataLoader";
	private final LocationRepository locationRepository;

	public LocationDataLoader() {
		locationRepository = new LocationRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return LOCATION_DATA_LOADER;
	}

	@Override
	protected LocationRepository getRepositoryInstance() {
		return locationRepository;
	}
}
