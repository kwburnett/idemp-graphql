package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class ChargeInput extends MCharge_BH {

	private AccountInput account;

	public ChargeInput() {
		super(Env.getCtx(), 0, null);
	}

	public ChargeInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public ChargeInput(Properties ctx, int C_BPartner_ID, String trxName) {
		super(ctx, C_BPartner_ID, trxName);
	}

	public void setIsLocked(boolean isLocked) {
		this.setBH_Locked(isLocked);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setAccount(AccountInput account) {
		this.account = account;
	}

	public AccountInput getAccount() {
		return account;
	}
}
