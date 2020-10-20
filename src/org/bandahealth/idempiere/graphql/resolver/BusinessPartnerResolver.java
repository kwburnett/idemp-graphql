package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.graphql.dataloader.OrderDataLoader;
import org.compiere.model.MLocation;
import org.dataloader.DataLoader;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

public class BusinessPartnerResolver extends BaseResolver<MBPartner_BH> implements GraphQLResolver<MBPartner_BH> {
	public String patientNumber(MBPartner_BH entity) {
		return entity.getBH_PatientID();
	}

	public Timestamp dateOfBirth(MBPartner_BH entity) {
		return entity.getBH_Birthday();
	}

	public String phone(MBPartner_BH entity) {
		return entity.getBH_Phone();
	}

	// TODO: Update to use batching
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

	public CompletableFuture<Integer> totalVisits(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, Integer> salesOrderCountDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderDataLoader.SALES_ORDER_COUNT_DATA_LOADER);
		return salesOrderCountDataLoader.load(entity.getC_BPartner_ID());
	}

	// TODO: Add sales orders to this entity
	public String lastVisitDate(MBPartner_BH entity) {
//		return entity.getla();
		return "never";
	}
}
