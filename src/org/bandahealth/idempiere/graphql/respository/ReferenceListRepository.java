package org.bandahealth.idempiere.graphql.respository;

import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MValRule;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;

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

	public MRefList getOrderPaymentType(String referenceValue) {
		return getTypes(ORDER_PAYMENT_TYPE, referenceValue).get(0);
	}

	public MRefList getReferral(String referenceValue) {
		return getTypes(REFERRAL_DROPDOWN, referenceValue).get(0);
	}

	public MRefList getInvoicePaymentType(String referenceValue) {
		return getTypes(INVOICE_PAYMENT_TYPE, referenceValue).get(0);
	}

	/**
	 * Get Reference List from MRefList.Table_Name
	 *
	 * @param referenceName
	 * @param referenceValue
	 * @return
	 */
	private List<MRefList> getTypes(String referenceName, String referenceValue) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(referenceName);

		String whereClause = MReference.Table_Name + "." + MReference.COLUMNNAME_Name + "=? ";

		if (referenceValue != null) {
			whereClause += " AND " + MRefList.COLUMNNAME_Value + "=?";
			parameters.add(referenceValue);
		}

		if (referenceName.equalsIgnoreCase(ORDER_PAYMENT_TYPE)) {
			// get payment type limits..
			MValRule valRule = new Query(Env.getCtx(), MValRule.Table_Name, MValRule.COLUMNNAME_Name + "=?", null)
					.setParameters(PAYMENT_TYPE_LIMIT).setOnlyActiveRecords(true).first();
			if (valRule != null) {
				whereClause += " AND " + valRule.getCode();
			}
		}

		return new Query(Env.getCtx(), MRefList.Table_Name, whereClause, null)
				.addJoinClause("JOIN " + MReference.Table_Name + " ON " + MReference.Table_Name + "."
						+ MReference.COLUMNNAME_AD_Reference_ID + "=" + MRefList.Table_Name + "."
						+ MRefList.COLUMNNAME_AD_Reference_ID)
				.setParameters(parameters).setOnlyActiveRecords(true).list();
	}
}
