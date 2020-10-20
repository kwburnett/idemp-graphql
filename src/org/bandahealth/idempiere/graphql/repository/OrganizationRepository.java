package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MOrg;
import org.compiere.util.Env;

public class OrganizationRepository extends BaseRepository<MOrg> {

	@Override
	public MOrg getModelInstance() {
		return new MOrg(Env.getCtx(), 0, null);
	}
}
