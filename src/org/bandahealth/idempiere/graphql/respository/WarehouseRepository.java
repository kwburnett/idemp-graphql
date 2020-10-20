package org.bandahealth.idempiere.graphql.respository;

import org.compiere.model.MWarehouse;
import org.compiere.util.Env;

public class WarehouseRepository extends BaseRepository<MWarehouse> {
	@Override
	public MWarehouse getModelInstance() {
		return new MWarehouse(Env.getCtx(), 0, null);
	}
}
