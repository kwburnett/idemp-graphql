package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MLocation;
import org.compiere.util.Env;

public class LocationRepository extends BaseRepository<MLocation, LocationInput> {
	@Override
	protected MLocation createModelInstance() {
		return new MLocation(Env.getCtx(), 0, null);
	}

	@Override
	public MLocation mapInputModelToModel(LocationInput entity) {
		MLocation location = getByUuid(entity.getC_Location_UU());
		if (location == null) {
			location = createModelInstance();
		}
		ModelUtil.setPropertyIfPresent(entity.getAddress1(), location::setAddress1);
		return location;
	}
}
