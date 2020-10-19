package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository extends BaseRepository<MOrder_BH> {

	public List<MOrder_BH> getSalesOrders(String filter, String sort, int page, int pageSize) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		return super.get(filter, sort, page, pageSize, MOrder_BH.COLUMNNAME_IsSOTrx + "=?", parameters);
	}

	@Override
	public MOrder_BH getModelInstance() {
		return new MOrder_BH(Env.getCtx(), 0, null);
	}
}
