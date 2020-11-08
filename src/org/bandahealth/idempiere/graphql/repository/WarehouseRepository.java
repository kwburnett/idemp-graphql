package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MWarehouse;
import org.compiere.util.Env;

public class WarehouseRepository extends BaseRepository<MWarehouse, MWarehouse> {
	@Override
	protected MWarehouse createModelInstance() {
		return new MWarehouse(Env.getCtx(), 0, null);
	}

	@Override
	public MWarehouse mapInputModelToModel(MWarehouse entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
