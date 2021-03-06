package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.ReferenceListInput;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MValRule;
import org.compiere.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReferenceListRepository extends BaseRepository<MRefList, ReferenceListInput> {

	public final static String PATIENT_TYPE = "BH_PatientType";
	public final static String ORDER_PAYMENT_TYPE = "C_Payment Tender Type";
	public final static String INVOICE_PAYMENT_TYPE = "_Payment Rule";
	public final static String NHIF_TYPE = "BH_NHIFTypeRef";
	public final static String NHIF_RELATIONSHIP = "BH_NHIF_Relationship_Choices";
	public final static String REFERRAL_DROPDOWN = "BH_Referral_Dropdown";
	public final static String PAYMENT_TYPE_LIMIT = "C_Payment Tender Type Limit";
	public final static String DOCUMENT_STATUS = "_Document Status";
	public final static String PRODUCT_CATEGORY_TYPE = "BH Product Category Type";

	public CompletableFuture<Map<String, MRefList>> getOrderPaymentType(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(ORDER_PAYMENT_TYPE, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getPatientType(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(PATIENT_TYPE, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getReferral(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(REFERRAL_DROPDOWN, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getInvoicePaymentType(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(INVOICE_PAYMENT_TYPE, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getNhifType(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(NHIF_TYPE, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getNhifRelationship(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(NHIF_RELATIONSHIP, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getDocumentStatus(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(DOCUMENT_STATUS, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public CompletableFuture<Map<String, MRefList>> getProductCategoryType(Set<String> referenceValues,
			Properties idempiereContext) {
		return CompletableFuture.supplyAsync(() -> getTypes(PRODUCT_CATEGORY_TYPE, referenceValues, idempiereContext).stream()
				.collect(Collectors.toMap(MRefList::getValue, ref -> ref)));
	}

	public List<MRefList> getTypes(String referenceName, Properties idempiereContext) {
		return getTypes(referenceName, null, idempiereContext);
	}

	/**
	 * Get Reference List from MRefList.Table_Name
	 *
	 * @param referenceName    A reference name, defined as a static on this class
	 * @param referenceValues  A list of values to fetch data for
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return The reference list data
	 */
	private List<MRefList> getTypes(String referenceName, Set<String> referenceValues, Properties idempiereContext) {
		List<MRefList> cachedValues = (List<MRefList>) cache.get(referenceName);
		if (cachedValues == null) {
			List<Object> parameters = new ArrayList<>();

			String whereClause = MReference.Table_Name + "." + MReference.COLUMNNAME_Name + "=? ";
			parameters.add(referenceName);

			if (referenceName.equalsIgnoreCase(ORDER_PAYMENT_TYPE)) {
				// get payment type limits..
				MValRule valRule = new Query(idempiereContext, MValRule.Table_Name, MValRule.COLUMNNAME_Name + "=?", null)
						.setParameters(PAYMENT_TYPE_LIMIT).setOnlyActiveRecords(true).setNoVirtualColumn(true).first();
				if (valRule != null) {
					whereClause += " AND " + valRule.getCode();
				}
			}

			cachedValues = new Query(idempiereContext, MRefList.Table_Name, whereClause, null)
					.addJoinClause("JOIN " + MReference.Table_Name + " ON " + MReference.Table_Name + "."
							+ MReference.COLUMNNAME_AD_Reference_ID + "=" + MRefList.Table_Name + "."
							+ MRefList.COLUMNNAME_AD_Reference_ID)
					.setParameters(parameters).setOnlyActiveRecords(true).setNoVirtualColumn(true)
					.list();
			cache.set(referenceName, cachedValues);
		}
		if (referenceValues == null) {
			return cachedValues;
		}
		return cachedValues.stream().filter(referenceList -> referenceValues.contains(referenceList.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	protected MRefList createModelInstance(Properties idempiereContext) {
		return new MRefList(idempiereContext, 0, null);
	}

	@Override
	public MRefList mapInputModelToModel(ReferenceListInput entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
