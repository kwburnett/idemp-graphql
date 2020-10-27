package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MProcess;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ProcessInput extends MProcess {
	public ProcessInput() {
		super(Env.getCtx(), 0, null);
	}

	public ProcessInput(Properties ctx, int AD_Process_ID, String trxName) {
		super(ctx, AD_Process_ID, trxName);
	}

	public ProcessInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
