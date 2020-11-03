package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MRoleIncluded;
import org.compiere.util.Env;

public class RoleIncludedRepository extends BaseRepository<MRoleIncluded, MRoleIncluded> {
	@Override
	public MRoleIncluded getModelInstance() {
		return new MRoleIncluded(Env.getCtx(), 0, null);
	}

	@Override
	public MRoleIncluded mapInputModelToModel(MRoleIncluded entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
