package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.QueryUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCurrency;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PaymentRepository extends BaseRepository<MPayment_BH, PaymentInput> {

	private final String CURRENCY = "KES";
	private final BusinessPartnerRepository businessPartnerRepository;

	public PaymentRepository() {
		businessPartnerRepository = new BusinessPartnerRepository();
	}

	public CompletableFuture<Map<Integer, List<MPayment_BH>>> getByOrderIds(Set<Integer> orderIds) {
		List<Object> parameters = new ArrayList<>();
		QueryUtil.getWhereClauseAndSetParametersForSet(orderIds, parameters);
		String whereCondition = QueryUtil.getWhereClauseAndSetParametersForSet(orderIds, parameters);
		List<MPayment_BH> payments = getBaseQuery(MPayment_BH.COLUMNNAME_C_Order_ID + " IN (" +
				whereCondition + ") OR " + MPayment_BH.COLUMNNAME_BH_C_Order_ID + " IN (" + whereCondition + ")", parameters)
				.setOnlyActiveRecords(true).list();
		return CompletableFuture.supplyAsync(() ->
				payments.stream().collect(Collectors.groupingBy(payment ->
						payment.getC_Order_ID() == 0 ? payment.getBH_C_Order_ID() : payment.getC_Order_ID())));
	}

	@Override
	public MPayment_BH getModelInstance() {
		return new MPayment_BH(Env.getCtx(), 0, null);
	}

	@Override
	public MPayment_BH save(PaymentInput entity) {
		MPayment_BH payment = getByUuid(entity.getC_Payment_UU());
		if (payment == null) {
			payment = getModelInstance();
		}

		if (entity.getC_Order_ID() > 0) {
			payment.setBH_C_Order_ID(entity.getC_Order_ID());
		} else {
			payment.setBH_IsServiceDebt(true);
		}

		if (entity.getBusinessPartner() != null) {
			MBPartner_BH bPartner = businessPartnerRepository.getByUuid(entity.getBusinessPartner().getC_BPartner_UU());
			if (bPartner != null) {
				payment.setC_BPartner_ID(bPartner.get_ID());
			}
		}

		if (entity.getC_Charge_ID() > 0) {
			payment.setC_Charge_ID(entity.getC_Charge_ID());
		}

		if (entity.getPayAmt() != null && entity.getPayAmt().compareTo(BigDecimal.ZERO) >= 0) {
			payment.setPayAmt(entity.getPayAmt());
		}

		if (entity.getPaymentType() != null) {
			payment.setTenderType(entity.getPaymentType().getValue());
		}

		// get currency
		MCurrency currency = getCurrency();
		if (currency != null) {
			payment.setC_Currency_ID(currency.get_ID());
		}

		// get bank account
		MBankAccount bankAccount = getBankAccount();
		if (bankAccount != null) {
			payment.setC_BankAccount_ID(bankAccount.get_ID());
		}

		// check nhif
		if (entity.getNhifType() != null && !StringUtil.isNullOrEmpty(entity.getNhifType().getValue())) {
			payment.setBH_NHIF_Type(entity.getNhifType().getValue());

			if (entity.getNhifRelationship() != null) {
				payment.setbh_nhif_relationship(entity.getNhifRelationship().getValue());
			}

			payment.setbh_nhif_member_name(entity.getbh_nhif_member_name());
			payment.setNHIF_Number(entity.getNHIF_Number());
			payment.setbh_nhif_member_id(entity.getbh_nhif_member_id());
			payment.setbh_nhif_claim_number(entity.getbh_nhif_claim_number());
		}

		// check description
		ModelUtil.setPropertyIfPresent(entity.getDescription(), payment::setDescription);
		ModelUtil.setPropertyIfPresent(entity.getDateTrx(), payment::setDateTrx);
		ModelUtil.setPropertyIfPresent(entity.isActive(), payment::setIsActive);

		payment.saveEx();

		cache.delete(payment.get_ID());

		return getByUuid(payment.getC_Payment_UU());
	}

	public void deleteByOrder(int orderId, List<String> paymentUuidsToKeep) {
		String whereClause = "(" + MPayment_BH.COLUMNNAME_C_Order_ID + "=? OR " + MPayment_BH.COLUMNNAME_BH_C_Order_ID +
				"=?)";
		if (paymentUuidsToKeep != null && !paymentUuidsToKeep.isEmpty()) {
			whereClause += " AND " + MPayment_BH.COLUMNNAME_C_Payment_UU + " NOT IN(" +
					paymentUuidsToKeep.stream().map(paymentUuidToKeep -> "'" + paymentUuidToKeep + "'")
							.collect(Collectors.joining(",")) + ")";
		}

		List<MPayment_BH> payments = new Query(Env.getCtx(), MPayment_BH.Table_Name, whereClause, null)
				.setParameters(orderId, orderId).setClient_ID().list();
		payments.forEach(payment -> payment.deleteEx(false));
	}

	/**
	 * Get client's currency in the db
	 *
	 * @return
	 */
	private MCurrency getCurrency() {
		// first check the currency from the client's accounting schema.
		int currencyId = 0;
		MAcctSchema[] schema = MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()), null);
		if (schema.length > 0) {
			currencyId = schema[0].getC_Currency_ID();
		}

		if (currencyId > 0) {
			return new MCurrency(Env.getCtx(), currencyId, null);
		}

		return new Query(Env.getCtx(), MCurrency.Table_Name, MCurrency.COLUMNNAME_ISO_Code + " = ?", null)
				.setParameters(CURRENCY).setOnlyActiveRecords(true).setClient_ID().first();
	}

	/**
	 * Get bank account
	 *
	 * @return
	 */
	protected MBankAccount getBankAccount() {
		MBankAccount bankAccount = new Query(Env.getCtx(), MBankAccount.Table_Name, null, null)
				.setOnlyActiveRecords(true).setClient_ID().setOrderBy(MBankAccount.COLUMNNAME_IsDefault + " DESC")
				.first();
		return bankAccount;
	}
}
