package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class BusinessPartnerRepository {
	public MBPartner_BH getById(int id) {
		return new Query(Env.getCtx(), MBPartner_BH.Table_Name, MBPartner_BH.COLUMNNAME_C_BPartner_ID + "=?",
				null).setParameters(id).first();
	}
}
