package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.dataloader.BusinessPartnerDataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.OrderLineDataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.PaymentDataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.ReferenceListDataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.DocStatus;
import org.bandahealth.idempiere.graphql.model.OrderStatus;
import org.bandahealth.idempiere.graphql.respository.BusinessPartnerRepository;
import org.bandahealth.idempiere.graphql.respository.OrderLineRepository;
import org.bandahealth.idempiere.graphql.respository.PaymentRepository;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderResolver extends BaseResolver<MOrder_BH> implements GraphQLResolver<MOrder_BH> {

	private final BusinessPartnerRepository businessPartnerRepository;
	private final OrderLineRepository orderLineRepository;
	private final PaymentRepository paymentRepository;
	private final ReferenceListRepository referenceListRepository;

	public OrderResolver() {
		businessPartnerRepository = new BusinessPartnerRepository();
		orderLineRepository = new OrderLineRepository();
		paymentRepository = new PaymentRepository();
		referenceListRepository = new ReferenceListRepository();
	}

	public CompletableFuture<MBPartner_BH> businessPartner(MOrder_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MBPartner_BH> businessPartnerDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(BusinessPartnerDataLoaderRegisterer.BUSINESS_PARTNER_DATA_LOADER_NAME);
		return businessPartnerDataLoader.load(entity.getC_BPartner_ID());
	}

	public CompletableFuture<List<MOrderLine_BH>> orderLines(MOrder_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MOrderLine_BH>> orderLineDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderLineDataLoaderRegisterer.ORDER_LINE_DATA_LOADER_NAME);
		return orderLineDataLoader.load(entity.getC_Order_ID());
	}

	public CompletableFuture<List<MPayment_BH>> payments(MOrder_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MPayment_BH>> paymentDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(PaymentDataLoaderRegisterer.PAYMENT_DATA_LOADER_NAME);
		return paymentDataLoader.load(entity.getC_Order_ID());
	}

	public boolean isSalesOrderTransaction(MOrder_BH entity) {
		return entity.isSOTrx();
	}

	public boolean isExpense(MOrder_BH entity) {
		return (boolean) entity.getBH_Isexpense();
	}

	public DocStatus docStatus(MOrder_BH entity) {
		return DocStatus.valueOf(entity.getDocStatus());
	}

	public boolean isNewVisit(MOrder_BH entity) {
		return entity.isBH_NewVisit();
	}

	public String visitNotes(MOrder_BH entity) {
		return "";
	}

	public CompletableFuture<MRefList> patientType(MOrder_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> referenceListPatientTypeDataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(ReferenceListDataLoaderRegisterer.PATIENT_TYPE_DATA_LOADER_NAME);
		return referenceListPatientTypeDataLoader.load(entity.getBH_PatientType());
	}

	public CompletableFuture<MRefList> referral(MOrder_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> referenceListReferralDataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(ReferenceListDataLoaderRegisterer.REFERRAL_DATA_LOADER_NAME);
		return referenceListReferralDataLoader.load(entity.getbh_referral());
	}

	public CompletableFuture<OrderStatus> status(MOrder_BH entity, DataFetchingEnvironment environment) {
		return getOrderStatus(entity, environment);
	}

	public String chiefComplaint(MOrder_BH entity) {
		return entity.getBH_Chief_Complaint();
	}

	public String temperature(MOrder_BH entity) {
		return entity.getBH_Temperature();
	}

	public String pulse(MOrder_BH entity) {
		return entity.getBH_Pulse();
	}

	public String respiratoryRate(MOrder_BH entity) {
		return entity.getBH_Respiratory_Rate();
	}

	public String bloodPressure(MOrder_BH entity) {
		return entity.getBH_Blood_Pressure();
	}

	public String height(MOrder_BH entity) {
		return entity.getBH_Height();
	}

	public String weight(MOrder_BH entity) {
		return entity.getBH_Weight();
	}

	public String secondDiagnosis(MOrder_BH entity) {
		return entity.getBH_SecondDiagnosis();
	}

	/**
	 * WAITING - visit with no clinical, no line items, no payments DISPENSING -
	 * visit with clinical information, no line items, no payments PENDING - visit
	 * with clinical information, line items, no payments, PENDING_COMPLETION -
	 * visit yet to be processed, COMPLETED - completed visit
	 *
	 * @param entity
	 */
	private CompletableFuture<OrderStatus> getOrderStatus(MOrder_BH entity, DataFetchingEnvironment environment) {
		CompletableFuture<List<MPayment_BH>> paymentFuture = payments(entity, environment);
		return orderLines(entity, environment).thenCombine(paymentFuture, (orderLines, payments) -> {
			// check payments
			boolean paymentsExist = payments != null && !payments.isEmpty();

			// check orderlines
			boolean orderlinesExist = orderLines != null && !orderLines.isEmpty();

			if (!orderlinesExist && !paymentsExist) {
				// check visit information
				if (entity.getbh_referral() == null && entity.getDescription() == null
						&& entity.getbh_lab_notes() == null) {
					return OrderStatus.WAITING;
				} else {
					return OrderStatus.DISPENSING;
				}
			} else if (orderlinesExist && !paymentsExist) {
				return OrderStatus.PENDING;
			} else {
				if (MOrder_BH.DOCSTATUS_Completed.equalsIgnoreCase(entity.getDocStatus())) {
					return OrderStatus.COMPLETED;
				} else {
					return OrderStatus.PENDING_COMPLETION;
				}
			}
		});
	}
}
