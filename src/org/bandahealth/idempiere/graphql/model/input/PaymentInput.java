package org.bandahealth.idempiere.graphql.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.utils.DateUtil;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

@JsonIgnoreProperties({"c_DocType_ID"})
public class PaymentInput extends MPayment_BH {

	private BusinessPartnerInput businessPartner;
	private ChargeInput charge;
	private ReferenceListInput paymentType;
	private ReferenceListInput nhifType;
	private ReferenceListInput nhifRelationship;

	public PaymentInput() {
		super(Env.getCtx(), 0, null);
	}

	public PaymentInput(Properties ctx, int C_Payment_ID, String trxName) {
		super(ctx, C_Payment_ID, trxName);
	}

	public PaymentInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setBusinessPartner(BusinessPartnerInput businessPartner) {
		this.businessPartner = businessPartner;
	}

	public BusinessPartnerInput getBusinessPartner() {
		return businessPartner;
	}

	public void setCharge(ChargeInput charge) {
		this.charge = charge;
	}

	public ChargeInput getCharge() {
		return charge;
	}

	public void setPaymentType(ReferenceListInput paymentType) {
		this.paymentType = paymentType;
		if (paymentType != null) {
			setTenderType(paymentType.getValue());
		}
	}

	public ReferenceListInput getPaymentType() {
		return paymentType;
	}

	public void setNhifType(ReferenceListInput nhifType) {
		this.nhifType = nhifType;
		if (nhifType != null) {
			setBH_NHIF_Type(nhifType.getValue());
		}
	}

	public ReferenceListInput getNhifType() {
		return nhifType;
	}

	public void setNhifRelationship(ReferenceListInput nhifRelationship) {
		this.nhifRelationship = nhifRelationship;
		if (nhifRelationship != null) {
			setbh_nhif_relationship(nhifRelationship.getValue());
		}
	}

	public ReferenceListInput getNhifRelationship() {
		return nhifRelationship;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.setPayAmt(payAmount);
	}

	public void setClaimNumber(String claimNumber) {
		this.setbh_nhif_claim_number(claimNumber);
	}

	public void setMemberId(String memberId) {
		this.setbh_nhif_member_id(memberId);
	}

	public void setNumber(String number) {
		this.setNHIF_Number(number);
	}

	public void setMemberName(String memberName) {
		this.setbh_nhif_member_name(memberName);
	}

	public void setTransactionDate(String transactionDate) {
		this.setDateTrx(DateUtil.getTimestamp(transactionDate));
	}

	public void setTenderAmount(BigDecimal tenderAmount) {
		this.setBH_TenderAmount(tenderAmount);
	}
}
