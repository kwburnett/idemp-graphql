package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MElementValue;
import org.compiere.model.X_I_ElementValue;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class AccountInput extends MElementValue {
	public AccountInput() {
		super(Env.getCtx(), 0, null);
	}

	public AccountInput(Properties ctx, int C_ElementValue_ID, String trxName) {
		super(ctx, C_ElementValue_ID, trxName);
	}

	public AccountInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public AccountInput(Properties ctx, String Value, String Name, String Description, String AccountType, String AccountSign, boolean IsDocControlled, boolean IsSummary, String trxName) {
		super(ctx, Value, Name, Description, AccountType, AccountSign, IsDocControlled, IsSummary, trxName);
	}

	public AccountInput(X_I_ElementValue imp) {
		super(imp);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
