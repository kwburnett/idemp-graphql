package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.base.model.MPayment_BH;
import org.bandahealth.idempiere.graphql.model.DocStatus;
import org.bandahealth.idempiere.graphql.model.OrderStatus;
import org.bandahealth.idempiere.graphql.respository.BusinessPartnerRepository;
import org.bandahealth.idempiere.graphql.respository.OrderLineRepository;
import org.bandahealth.idempiere.graphql.respository.PaymentRepository;
import org.bandahealth.idempiere.graphql.respository.ReferenceListRepository;
import org.compiere.model.MRefList;
import org.compiere.util.Env;

import java.util.List;

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

	public MBPartner_BH businessPartner(MOrder_BH entity) {
		return businessPartnerRepository.getById(entity.getC_BPartner_ID());
	}

	public List<MOrderLine_BH> orderLines(MOrder_BH entity) {
		return orderLineRepository.getByOrder(entity.getC_Order_ID());
	}

	public List<MPayment_BH> payments(MOrder_BH entity) {
		return paymentRepository.getByOrder(entity.getC_Order_ID());
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

	public MRefList patientType(MOrder_BH entity) {
		return referenceListRepository.getOrderPaymentType(entity.getPaymentRule());
	}

	public MRefList referral(MOrder_BH entity) {
//		return referenceListRepository.getReferral(entity.getbh_r());
		return new MRefList(Env.getCtx(), 0, null);
	}

	public OrderStatus status(MOrder_BH entity) {
//		return referenceListRepository.getReferral(entity.getbh_r());
		return getOrderStatus(entity);
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
	private OrderStatus getOrderStatus(MOrder_BH entity) {
		// check payments
		boolean paymentsExist = !payments(entity).isEmpty();

		// check orderlines
		boolean orderlinesExist = !orderLines(entity).isEmpty();

		if (!orderlinesExist && !paymentsExist) {
			// check visit information
//			if (entity.get_Value(COLUMNNAME_REFERRAL) == null && entity.getDescription() == null
//					&& entity.get_Value(COLUMNNAME_VISIT_NOTES) == null) {
			if (entity.getDescription() == null) {
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
	}
}
