package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PaymentRepository extends BaseRepository<MPayment_BH> {

	public CompletableFuture<Map<Integer, List<MPayment_BH>>> getByOrderIds(Set<Integer> orderIds) {
		List<Object> parameters = new ArrayList<>();
		QueryUtil.getWhereClauseAndSetParametersForSet(orderIds, parameters);
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(orderIds, parameters);
		List<MPayment_BH> payments = new Query(Env.getCtx(), MPayment_BH.Table_Name,
				MPayment_BH.COLUMNNAME_C_Order_ID + " IN (" + whereCondition + ") OR "
						+ MPayment_BH.COLUMNNAME_BH_C_Order_ID + " IN (" + whereCondition + ")", null)
				.setParameters(parameters).setOnlyActiveRecords(true).list();
		return CompletableFuture.supplyAsync(() ->
				payments.stream().collect(Collectors.groupingBy(MPayment_BH::getC_Order_ID)));
	}

	@Override
	public MPayment_BH getModelInstance() {
		return new MPayment_BH(Env.getCtx(), 0, null);
	}
}
