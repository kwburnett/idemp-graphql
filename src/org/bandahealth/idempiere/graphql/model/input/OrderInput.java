package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MProject;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public class OrderInput extends MOrder_BH {

	private BusinessPartnerInput businessPartner;
	private List<OrderLineInput> orderLines;
	private List<PaymentInput> payments;
	private ReferenceListInput patientType;
	private ReferenceListInput referral;

	public OrderInput() {
		super(Env.getCtx(), 0, null);
	}

	public OrderInput(Properties ctx, int C_Order_ID, String trxName) {
		super(ctx, C_Order_ID, trxName);
	}

	public OrderInput(MProject project, boolean IsSOTrx, String DocSubTypeSO) {
		super(project, IsSOTrx, DocSubTypeSO);
	}

	public OrderInput(Properties ctx, ResultSet rs, String trxName) {
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

	public void setOrderLines(List<OrderLineInput> orderLines) {
		this.orderLines = orderLines;
	}

	public List<OrderLineInput> getOrderLines() {
		return orderLines;
	}

	public void setPayments(List<PaymentInput> payments) {
		this.payments = payments;
	}

	public List<PaymentInput> getPayments() {
		return payments;
	}

	public void setPatientType(ReferenceListInput patientType) {
		this.patientType = patientType;
		if (patientType != null) {
			setBH_PatientType(patientType.getValue());
		}
	}

	public ReferenceListInput getPatientType() {
		return patientType;
	}

	public void setReferral(ReferenceListInput referral) {
		this.referral = referral;
		if (referral != null) {
			setbh_referral(referral.getValue());
		}
	}

	public ReferenceListInput getReferral() {
		return referral;
	}

	public void setIsSalesOrderTransaction(Boolean isSalesOrderTransaction) {
		this.setIsSOTrx(isSalesOrderTransaction);
	}

	public void setIsExpense(boolean isExpense) {
		this.setBH_Isexpense(isExpense);
	}

	public void setNewVisit(boolean newVisit) {
		this.setBH_NewVisit(newVisit);
	}

	public void setVisitNotes(String visitNotes) {
		this.setbh_lab_notes(visitNotes);
	}

	public void setChiefComplaint(String chiefComplaint) {
		this.setBH_Chief_Complaint(chiefComplaint);
	}

	public void setTemperature(String temperature) {
		this.setBH_Temperature(temperature);
	}

	public void setPulse(String pulse) {
		this.setBH_Pulse(pulse);
	}

	public void setRespiratoryRate(String respiratoryRate) {
		this.setBH_Respiratory_Rate(respiratoryRate);
	}

	public void setBloodPressure(String bloodPressure) {
		this.setBH_Blood_Pressure(bloodPressure);
	}

	public void setHeight(String height) {
		this.setBH_Height(height);
	}

	public void setWeight(String weight) {
		this.setBH_Weight(weight);
	}

	public void setSecondDiagnosis(String secondDiagnosis) {
		this.setBH_SecondDiagnosis(secondDiagnosis);
	}

	public void copyTo(MOrder_BH order) {
		ModelUtil.setPropertyIfPresent(getbh_lab_notes(), order::setbh_lab_notes);
		ModelUtil.setPropertyIfPresent(getDescription(), order::setDescription);

		if (getPatientType() != null) {
			ModelUtil.setPropertyIfPresent(getPatientType().getName(), order::setBH_PatientType);
		}
		if (getReferral() != null) {
			ModelUtil.setPropertyIfPresent(getReferral().getName(), order::setbh_referral);
		}

		ModelUtil.setPropertyIfPresent(isBH_NewVisit(), order::setBH_NewVisit);
		ModelUtil.setPropertyIfPresent(getBH_Chief_Complaint(), order::setBH_Chief_Complaint);
		ModelUtil.setPropertyIfPresent(getBH_Temperature(), order::setBH_Temperature);
		ModelUtil.setPropertyIfPresent(getBH_Pulse(), order::setBH_Pulse);
		ModelUtil.setPropertyIfPresent(getBH_Respiratory_Rate(), order::setBH_Respiratory_Rate);
		ModelUtil.setPropertyIfPresent(getBH_Blood_Pressure(), order::setBH_Blood_Pressure);
		ModelUtil.setPropertyIfPresent(getBH_Height(), order::setBH_Height);
		ModelUtil.setPropertyIfPresent(getBH_Weight(), order::setBH_Weight);
		ModelUtil.setPropertyIfPresent(getBH_SecondDiagnosis(), order::setBH_SecondDiagnosis);
		ModelUtil.setPropertyIfPresent(isSOTrx(), order::setIsSOTrx);
	}
}
