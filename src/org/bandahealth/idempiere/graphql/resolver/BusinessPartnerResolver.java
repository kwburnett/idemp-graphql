package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.utils.DateUtil;
import org.compiere.model.MLocation;

public class BusinessPartnerResolver extends BaseResolver<MBPartner_BH> implements GraphQLResolver<MBPartner_BH> {
	public String patientNumber(MBPartner_BH entity) {
		return entity.getBH_PatientID();
	}

	public String dateOfBirth(MBPartner_BH entity) {
		return DateUtil.parseDateOnly(entity.getBH_Birthday());
	}

	public String phone(MBPartner_BH entity) {
		return entity.getBH_Phone();
	}

	public MLocation location(MBPartner_BH entity) {
		return (MLocation) entity.getBH_C_Location();
	}

	public String gender(MBPartner_BH entity) {
		return entity.getbh_gender();
	}

	public String email(MBPartner_BH entity) {
		return entity.getBH_EMail();
	}

	public String nhifRelationship(MBPartner_BH entity) {
		return entity.getbh_nhif_relationship();
	}

	public String nhifMemberName(MBPartner_BH entity) {
		return entity.getbh_nhif_member_name();
	}

	public String nhifNumber(MBPartner_BH entity) {
		return entity.getNHIF_Number();
	}

	public String nhifType(MBPartner_BH entity) {
		return entity.getBH_NHIF_Type();
	}

	public String nationalId(MBPartner_BH entity) {
		return entity.getNational_ID();
	}

	public String occupation(MBPartner_BH entity) {
		return entity.getbh_occupation();
	}

	public String nextOfKinName(MBPartner_BH entity) {
		return entity.getNextOfKin_Name();
	}

	public String nextOfKinContact(MBPartner_BH entity) {
		return entity.getNextOfKin_Contact();
	}

	public String localPatientNumber(MBPartner_BH entity) {
		return entity.getBH_Local_PatientID();
	}

	// TODO: Add sales orders to this entity
	public int totalVisits(MBPartner_BH entity) {
//		return entity.gettot();
		return 0;
	}

	// TODO: Add sales orders to this entity
	public String lastVisitDate(MBPartner_BH entity) {
//		return entity.getla();
		return "never";
	}
}
