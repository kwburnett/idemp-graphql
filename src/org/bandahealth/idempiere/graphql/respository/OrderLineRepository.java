package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrderLineRepository {

	public CompletableFuture<Map<Integer, List<MOrderLine_BH>>> getByOrderIds(Set<Integer> orderIds) {
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(orderIds, parameters);
		List<MOrderLine_BH> orderLines = new Query(Env.getCtx(),MOrderLine_BH.Table_Name,
				MOrderLine_BH.COLUMNNAME_C_Order_ID + " IN (" + whereCondition + ")", null)
				.setParameters(parameters)
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.list();
		return CompletableFuture.supplyAsync(() ->
				orderLines.stream().collect(Collectors.groupingBy(MOrderLine_BH::getC_Order_ID)));
	}
}
