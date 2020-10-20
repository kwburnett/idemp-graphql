package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;

public class UserRepository extends BaseRepository<MUser_BH> {
	@Override
	public MUser_BH getModelInstance() {
		return new MUser_BH(Env.getCtx(), 0, null);
	}
}
