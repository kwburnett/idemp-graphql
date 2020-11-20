package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.LocationDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.PaymentDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ReferenceListDataLoader;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MLocation;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Business Partner resolver containing specific methods to fetch non-standard iDempiere properties for the consumer
 */
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
				environment.getDataLoaderRegistry().getDataLoader(LocationDataLoader.LOCATION_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getBH_C_Location_ID());
	}

	public String gender(MBPartner_BH entity) {
		return entity.getbh_gender();
	}

	public String email(MBPartner_BH entity) {
		return entity.getBH_EMail();
	}

	public CompletableFuture<MRefList> nhifRelationship(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ReferenceListDataLoader.NHIF_RELATIONSHIP_DATA_LOADER);
		if (StringUtil.isNullOrEmpty(entity.getbh_nhif_relationship())) {
			return null;
		}
		return dataLoader.load(entity.getbh_nhif_relationship());
	}

	public String nhifMemberName(MBPartner_BH entity) {
		return entity.getbh_nhif_member_name();
	}

	public String nhifNumber(MBPartner_BH entity) {
		return entity.getNHIF_Number();
	}

	public CompletableFuture<MRefList> nhifType(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ReferenceListDataLoader.NHIF_TYPE_DATA_LOADER);
		if (StringUtil.isNullOrEmpty(entity.getBH_NHIF_Type())) {
			return null;
		}
		return dataLoader.load(entity.getBH_NHIF_Type());
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
		final DataLoader<String, List<MOrder_BH>> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderDataLoader.SALES_ORDER_BY_BUSINESS_PARTNER_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getC_BPartner_ID()));
	}

	public CompletableFuture<Integer> totalVisits(MBPartner_BH entity, DataFetchingEnvironment environment) {
		return salesOrders(entity, environment).thenApply(orders -> orders == null ? 0 : orders.size());
	}

	public CompletableFuture<Timestamp> lastVisitDate(MBPartner_BH entity, DataFetchingEnvironment environment) {
		return salesOrders(entity, environment)
				.thenApply(orders -> orders == null ? null :
						orders.stream().map(MOrder_BH::getDateOrdered).max(Timestamp::compareTo).orElse(null));
	}

	public CompletableFuture<List<MPayment_BH>> payments(MBPartner_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, List<MPayment_BH>> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(PaymentDataLoader.PAYMENT_BY_BUSINESS_PARTNER_DATA_LOADER);
		return dataLoader.load(ModelUtil.getModelKey(entity, entity.getC_BPartner_ID()));
	}
}
