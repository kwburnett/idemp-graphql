package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MRoleIncluded;

import java.util.Properties;

public class RoleIncludedRepository extends BaseRepository<MRoleIncluded, MRoleIncluded> {
	@Override
	protected MRoleIncluded createModelInstance(Properties idempiereContext) {
		return new MRoleIncluded(idempiereContext, 0, null);
	}

	@Override
	public MRoleIncluded mapInputModelToModel(MRoleIncluded entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
