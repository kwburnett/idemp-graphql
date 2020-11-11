package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MWarehouse;

import java.util.Properties;

public class WarehouseRepository extends BaseRepository<MWarehouse, MWarehouse> {
	@Override
	protected MWarehouse createModelInstance(Properties idempiereContext) {
		return new MWarehouse(idempiereContext, 0, null);
	}

	@Override
	public MWarehouse mapInputModelToModel(MWarehouse entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
