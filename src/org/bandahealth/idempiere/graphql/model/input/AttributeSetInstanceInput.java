package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MAttributeSetInstance;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class AttributeSetInstanceInput extends MAttributeSetInstance {

	private AttributeSetInput attributeSet;

	public AttributeSetInstanceInput() {
		super(Env.getCtx(), 0, null);
	}

	public AttributeSetInstanceInput(Properties ctx, int M_AttributeSetInstance_ID, String trxName) {
		super(ctx, M_AttributeSetInstance_ID, trxName);
	}

	public AttributeSetInstanceInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public AttributeSetInstanceInput(Properties ctx, int M_AttributeSetInstance_ID, int M_AttributeSet_ID, String trxName) {
		super(ctx, M_AttributeSetInstance_ID, M_AttributeSet_ID, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setAttributeSet(AttributeSetInput attributeSet) {
		this.attributeSet = attributeSet;
	}

	public AttributeSetInput getAttributeSet() {
		return attributeSet;
	}
}
