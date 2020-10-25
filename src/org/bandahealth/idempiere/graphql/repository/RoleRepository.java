package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MRole;
import org.compiere.util.Env;

public class RoleRepository extends BaseRepository<MRole, MRole> {
	@Override
	public MRole getModelInstance() {
		return new MRole(Env.getCtx(), 0, null);
	}

	@Override
	public MRole save(MRole entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
