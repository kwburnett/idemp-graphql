package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MWarehouse;
import org.compiere.util.Env;

public class WarehouseRepository extends BaseRepository<MWarehouse, MWarehouse> {
	@Override
	public MWarehouse getModelInstance() {
		return new MWarehouse(Env.getCtx(), 0, null);
	}

	@Override
	public MWarehouse save(MWarehouse entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
