package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MValRule;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReferenceListRepository {

	public final static String PATIENT_TYPE = "BH_PatientType";
	public final static String ORDER_PAYMENT_TYPE = "C_Payment Tender Type";
	public final static String INVOICE_PAYMENT_TYPE = "_Payment Rule";
	public final static String NHIF_TYPE = "BH_NHIFTypeRef";
	public final static String NHIF_RELATIONSHIP = "BH_NHIF_Relationship_Choices";
	public final static String REFERRAL_DROPDOWN = "BH_Referral_Dropdown";
	public final static String PAYMENT_TYPE_LIMIT = "C_Payment Tender Type Limit";
	public final static String DOCUMENT_STATUS = "_Document Status";
	public final static String PRODUCT_CATEGORY_TYPE = "BH Product Category Type";

	public CompletableFuture<Map<String, MRefList>> getOrderPaymentType(Set<String> referenceValues) {
		return getTypes(ORDER_PAYMENT_TYPE, referenceValues);
	}

	public CompletableFuture<Map<String, MRefList>> getPatientType(Set<String> referenceValues) {
		return getTypes(PATIENT_TYPE, referenceValues);
	}

	public CompletableFuture<Map<String, MRefList>> getReferral(Set<String> referenceValues) {
		return getTypes(REFERRAL_DROPDOWN, referenceValues);
	}

	public CompletableFuture<Map<String, MRefList>> getInvoicePaymentType(Set<String> referenceValues) {
		return getTypes(INVOICE_PAYMENT_TYPE, referenceValues);
	}

	/**
	 * Get Reference List from MRefList.Table_Name
	 *
	 * @param referenceName
	 * @param referenceValues
	 * @return
	 */
	private CompletableFuture<Map<String, MRefList>> getTypes(String referenceName, Set<String> referenceValues) {
		List<Object> parameters = new ArrayList<>();

		String whereClause = MReference.Table_Name + "." + MReference.COLUMNNAME_Name + "=? ";
		parameters.add(referenceName);

		whereClause += " AND " + MRefList.COLUMNNAME_Value + " IN ("
				+ QueryUtil.getWhereClauseAndSetParametersForSet(referenceValues, parameters) + ")";

		if (referenceName.equalsIgnoreCase(ORDER_PAYMENT_TYPE)) {
			// get payment type limits..
			MValRule valRule = new Query(Env.getCtx(), MValRule.Table_Name, MValRule.COLUMNNAME_Name + "=?", null)
					.setParameters(PAYMENT_TYPE_LIMIT).setOnlyActiveRecords(true).first();
			if (valRule != null) {
				whereClause += " AND " + valRule.getCode();
			}
		}

		List<MRefList> types = new Query(Env.getCtx(), MRefList.Table_Name, whereClause, null)
				.addJoinClause("JOIN " + MReference.Table_Name + " ON " + MReference.Table_Name + "."
						+ MReference.COLUMNNAME_AD_Reference_ID + "=" + MRefList.Table_Name + "."
						+ MRefList.COLUMNNAME_AD_Reference_ID)
				.setParameters(parameters).setOnlyActiveRecords(true)
				.list();
		return CompletableFuture.supplyAsync(() ->
				types.stream().collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}
}
