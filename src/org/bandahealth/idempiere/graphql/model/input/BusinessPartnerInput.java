package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.utils.DateUtil;
import org.compiere.model.X_I_BPartner;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class BusinessPartnerInput extends MBPartner_BH {
	private LocationInput location;
	private ReferenceListInput nhifType;
	private ReferenceListInput nhifRelationship;

	public BusinessPartnerInput() {
		super(Env.getCtx(), 0, null);
	}

	public BusinessPartnerInput(Properties ctx) {
		super(ctx);
	}

	public BusinessPartnerInput(X_I_BPartner impBP) {
		super(impBP);
	}

	public BusinessPartnerInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public BusinessPartnerInput(Properties ctx, int C_BPartner_ID, String trxName) {
		super(ctx, C_BPartner_ID, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setLocation(LocationInput location) {
		this.location = location;
	}

	public LocationInput getLocation() {
		return location;
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

	public void setPatientNumber(String patientNumber) {
		this.setBH_PatientID(patientNumber);
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.setBH_Birthday(DateUtil.getTimestamp(dateOfBirth));
	}

	public void setPhone(String phone) {
		this.setBH_Phone(phone);
	}

	public void setGender(String gender) {
		this.setbh_gender(gender);
	}

	public void setEmail(String email) {
		this.setBH_EMail(email);
	}

	public void setNhifMemberName(String nhifMemberName) {
		this.setbh_nhif_member_name(nhifMemberName);
	}

	public void setNhifNumber(String nhifNumber) {
		this.setNHIF_Number(nhifNumber);
	}

	public void setNationalId(String nationalId) {
		this.setNationalID(nationalId);
	}

	public void setOccupation(String occupation) {
		this.setbh_occupation(occupation);
	}

	public void setNextOfKinName(String nextOfKinName) {
		this.setNextOfKin_Name(nextOfKinName);
	}

	public void setNextOfKinContact(String nextOfKinContact) {
		this.setNextOfKin_Contact(nextOfKinContact);
	}

	public void setLocalPatientNumber(String localPatientNumber) {
		this.setBH_Local_PatientID(localPatientNumber);
	}
}
