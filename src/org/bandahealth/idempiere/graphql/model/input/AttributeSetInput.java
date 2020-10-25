package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MAttributeSet;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class AttributeSetInput extends MAttributeSet {
	public AttributeSetInput() {
		super(Env.getCtx(), 0, null);
	}

	public AttributeSetInput(Properties ctx, int M_AttributeSet_ID, String trxName) {
		super(ctx, M_AttributeSet_ID, trxName);
	}

	public AttributeSetInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}
}
