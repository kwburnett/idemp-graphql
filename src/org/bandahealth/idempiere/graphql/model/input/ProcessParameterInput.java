package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ProcessParameterInput extends MProcessPara {
	public ProcessParameterInput() {
		super(Env.getCtx(), 0, null);
	}

	public ProcessParameterInput(Properties ctx, int AD_Process_Para_ID, String trxName) {
		super(ctx, AD_Process_Para_ID, trxName);
	}

	public ProcessParameterInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public ProcessParameterInput(MProcess parent) {
		super(parent);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
