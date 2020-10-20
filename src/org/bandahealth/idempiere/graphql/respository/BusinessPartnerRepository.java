package org.bandahealth.idempiere.graphql.respository;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BusinessPartnerRepository {
	public CompletableFuture<Map<Integer, MBPartner_BH>> getByIds(Set<Integer> ids) {
		List<Object> parameters = new ArrayList<>();
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(ids, parameters);
		List<MBPartner_BH> businessPartners = new Query(Env.getCtx(), MBPartner_BH.Table_Name,
				MBPartner_BH.COLUMNNAME_C_BPartner_ID + " IN (" + whereCondition + ")",
				null)
				.setParameters(parameters).list();
		return CompletableFuture.supplyAsync(() ->
				businessPartners.stream().collect(
						Collectors.toMap(MBPartner_BH::getC_BPartner_ID, businessPartner -> businessPartner)));
	}
}
