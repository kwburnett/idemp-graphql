package org.bandahealth.idempiere.graphql.respository;

import org.compiere.model.MRole;
import org.compiere.util.Env;

public class RoleRepository extends BaseRepository<MRole> {
	@Override
	public MRole getModelInstance() {
		return new MRole(Env.getCtx(), 0, null);
	}
}
