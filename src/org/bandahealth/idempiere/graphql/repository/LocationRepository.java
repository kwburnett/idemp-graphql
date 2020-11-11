package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MLocation;

import java.util.Properties;

public class LocationRepository extends BaseRepository<MLocation, LocationInput> {
	@Override
	protected MLocation createModelInstance(Properties idempiereContext) {
		return new MLocation(idempiereContext, 0, null);
	}

	@Override
	public MLocation mapInputModelToModel(LocationInput entity, Properties idempiereContext) {
		MLocation location = getByUuid(entity.getC_Location_UU(), idempiereContext);
		if (location == null) {
			location = createModelInstance(idempiereContext);
		}
		ModelUtil.setPropertyIfPresent(entity.getAddress1(), location::setAddress1);
		return location;
	}
}
