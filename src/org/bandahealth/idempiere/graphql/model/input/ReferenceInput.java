package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MReference;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ReferenceInput extends MReference {
	public ReferenceInput() {
		super(Env.getCtx(), 0, null);
	}

	public ReferenceInput(Properties ctx, int AD_Reference_ID, String trxName) {
		super(ctx, AD_Reference_ID, trxName);
	}

	public ReferenceInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
