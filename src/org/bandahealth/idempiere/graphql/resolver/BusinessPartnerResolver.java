package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderDataLoader;
import org.compiere.model.MLocation;
import org.dataloader.DataLoader;

import java.sql.Timestamp;
import java.util.List;
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

	public CompletableFuture<MLocation> location(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MLocation> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(LocationDataLoader.LOCATION_DATA_LOADER);
		return dataLoader.load(entity.getBH_C_Location_ID());
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

	public CompletableFuture<List<MOrder_BH>> salesOrders(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MOrder_BH>> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderDataLoader.SALES_ORDER_BY_BUSINESS_PARTNER_DATA_LOADER);
		return dataLoader.load(entity.getC_BPartner_ID());
	}

	public CompletableFuture<Integer> totalVisits(MBPartner_BH entity, DataFetchingEnvironment environment) {
		return salesOrders(entity, environment).thenApply(List::size);
	}

	public CompletableFuture<Timestamp> lastVisitDate(MBPartner_BH entity, DataFetchingEnvironment environment) {
		return salesOrders(entity, environment)
				.thenApply(orders -> orders.stream().map(MOrder_BH::getDateOrdered).max(Timestamp::compareTo).orElse(null));
	}
}
