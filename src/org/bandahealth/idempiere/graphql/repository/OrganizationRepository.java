package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MOrg;
import org.compiere.util.Env;

import java.util.Properties;

public class OrganizationRepository extends BaseRepository<MOrg, MOrg> {

	@Override
	protected MOrg createModelInstance(Properties idempiereContext) {
		return new MOrg(idempiereContext, 0, null);
	}

	@Override
	public MOrg mapInputModelToModel(MOrg entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
