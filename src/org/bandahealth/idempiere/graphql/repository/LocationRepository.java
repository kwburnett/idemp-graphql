package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.LocationInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MLocation;
import org.compiere.util.Env;

public class LocationRepository extends BaseRepository<MLocation, LocationInput> {
	@Override
	public MLocation getModelInstance() {
		return new MLocation(Env.getCtx(), 0, null);
	}

	@Override
	public MLocation save(LocationInput entity) {
		MLocation location = getByUuid(entity.getC_Location_UU());
		if (location == null) {
			location = getModelInstance();
		}
		ModelUtil.setPropertyIfPresent(entity.getAddress1(), location::setAddress1);
		location.saveEx();
		cache.delete(location.get_ID());
		return getByUuid(location.getC_Location_UU());
	}
}
