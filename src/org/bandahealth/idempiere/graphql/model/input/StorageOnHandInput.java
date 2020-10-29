package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MStorageOnHand;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class StorageOnHandInput extends MStorageOnHand {
	public StorageOnHandInput() {
		super(Env.getCtx(), 0, null);
	}

	public StorageOnHandInput(Properties ctx, int ignored, String trxName) {
		super(ctx, ignored, trxName);
	}

	public StorageOnHandInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public BigDecimal getQuantityOnHand() {
		return getQtyOnHand();
	}

	public void setQuantityOnHand(BigDecimal quantityOnHand) {
		setQtyOnHand(quantityOnHand);
	}
}
