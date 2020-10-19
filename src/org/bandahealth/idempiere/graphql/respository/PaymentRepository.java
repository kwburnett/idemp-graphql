package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;

public class PaymentRepository {

	public List<MPayment_BH> getByOrder(int orderId) {
		return new Query(Env.getCtx(), MPayment_BH.Table_Name, MPayment_BH.COLUMNNAME_C_Order_ID + "=? OR "
				+ MPayment_BH.COLUMNNAME_BH_C_Order_ID + "=?", null)
				.setParameters(orderId, orderId).setOnlyActiveRecords(true).list();
	}
}
