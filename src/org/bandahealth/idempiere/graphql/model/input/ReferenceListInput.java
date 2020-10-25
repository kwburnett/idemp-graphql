package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MRefList;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ReferenceListInput extends MRefList {
	public ReferenceListInput() {
		super(Env.getCtx(), 0, null);
	}

	public ReferenceListInput(Properties ctx, int AD_Ref_List_ID, String trxName) {
		super(ctx, AD_Ref_List_ID, trxName);
	}

	public ReferenceListInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
