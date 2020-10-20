package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.util.Env;

public class UserRepository extends BaseRepository<MUser_BH> {
	@Override
	public MUser_BH getModelInstance() {
		return new MUser_BH(Env.getCtx(), 0, null);
	}
}
