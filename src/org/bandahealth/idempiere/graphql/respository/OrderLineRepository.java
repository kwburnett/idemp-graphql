package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;

public class OrderLineRepository {

	public List<MOrderLine_BH> getByOrder(int orderId) {
		return new Query(Env.getCtx(),MOrderLine_BH.Table_Name, MOrderLine_BH.COLUMNNAME_C_Order_ID + "=?",
				null).setParameters(orderId).setClient_ID().setOnlyActiveRecords(true).list();
	}
}
