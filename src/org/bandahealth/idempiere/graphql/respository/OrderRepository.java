package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.bandahealth.idempiere.graphql.utils.SqlUtil;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OrderRepository extends BaseRepository<MOrder_BH> {

	public List<MOrder_BH> getSalesOrders(String filter, String sort, int page, int pageSize) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		return super.get(filter, sort, page, pageSize, MOrder_BH.COLUMNNAME_IsSOTrx + "=?", parameters);
	}

	public CompletableFuture<Map<Integer, Integer>> getSalesOrderCountsByBusinessPartner(Set<Integer> businessPartnerIds) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(businessPartnerIds, parameters);
		parameters.add("Y");

		String sqlWhere = "WHERE " + MOrder_BH.COLUMNNAME_IsSOTrx + "=? AND " + MOrder_BH.COLUMNNAME_C_BPartner_ID + " IN ("
				+ whereCondition + ") AND " + MOrder_BH.COLUMNNAME_IsActive + "=?";
		Map<Integer, Integer> counts = SqlUtil
				.getGroupedCount(MOrder_BH.Table_Name, sqlWhere, parameters, MOrder_BH.COLUMNNAME_C_BPartner_ID,
						businessPartnerIds);
		return CompletableFuture.supplyAsync(() -> counts);
	}

	@Override
	public MOrder_BH getModelInstance() {
		return new MOrder_BH(Env.getCtx(), 0, null);
	}
}
