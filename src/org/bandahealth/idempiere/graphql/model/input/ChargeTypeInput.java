package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ChargeTypeInput extends MChargeType_BH {
	public ChargeTypeInput() {
		super(Env.getCtx(), 0, null);
	}

	public ChargeTypeInput(Properties ctx, int C_ChargeType_ID, String trxName) {
		super(ctx, C_ChargeType_ID, trxName);
	}

	public ChargeTypeInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
