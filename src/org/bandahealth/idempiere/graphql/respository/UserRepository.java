package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;

public class UserRepository {

	public List<MUser> getUsers() {
		return new Query(Env.getCtx(), MUser_BH.Table_Name, "AD_User_ID<1000001", null).list();
	}
}
