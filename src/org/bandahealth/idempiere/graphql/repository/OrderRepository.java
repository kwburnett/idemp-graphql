package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.model.input.OrderLineInput;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MDocType;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrderRepository extends BaseRepository<MOrder_BH, OrderInput> {

	private final CLogger logger = CLogger.getCLogger(OrderRepository.class);

	private final OrderLineRepository orderLineRepository;
	private final BusinessPartnerRepository businessPartnerRepository;
	private final PaymentRepository paymentRepository;
	private final ProcessRepository processRepository;

	private final String businessPartnerJoin = "JOIN " + MBPartner_BH.Table_Name + " ON " + MBPartner_BH.Table_Name +
			"." + MBPartner_BH.COLUMNNAME_C_BPartner_ID + "=" + MOrder_BH.Table_Name + "." +
			MOrder_BH.COLUMNNAME_C_BPartner_ID;

	public OrderRepository() {
		orderLineRepository = new OrderLineRepository();
		businessPartnerRepository = new BusinessPartnerRepository();
		paymentRepository = new PaymentRepository();
		processRepository = new ProcessRepository();
	}

	public Connection<MOrder_BH> getPurchaseOrders(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("N");

		return super.get(filter, sort, pagingInfo, MOrder_BH.COLUMNNAME_IsSOTrx + "=? AND " +
				MOrder_BH.COLUMNNAME_BH_IsExpense + " IS NULL", parameters, businessPartnerJoin, environment);
	}

	public Connection<MOrder_BH> getSalesOrders(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");

		return super.get(filter, sort, pagingInfo, MOrder_BH.COLUMNNAME_IsSOTrx + "=?", parameters,
				businessPartnerJoin, environment);
	}

	@Override
	public MOrder_BH getModelInstance() {
		return new MOrder_BH(Env.getCtx(), 0, null);
	}

	public MOrder_BH save(OrderInput entity) {
		try {
			MOrder_BH order = getByUuid(entity.getC_Order_UU());
			if (order == null) {
				order = getModelInstance();
			}

			ModelUtil.setPropertyIfPresent(entity.getDateOrdered(), order::setDateOrdered);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), order::setDescription);

			order.setIsActive(entity.isActive());
			order.setIsApproved(true);
			order.setDocAction(MOrder_BH.DOCACTION_Complete);

			MBPartner_BH businessPartner = null;

			// set patient
			if (entity.getBusinessPartner() != null && entity.getBusinessPartner().getC_BPartner_UU() != null) {
				businessPartner = businessPartnerRepository
						.getByUuid(entity.getBusinessPartner().getC_BPartner_UU());
				if (businessPartner != null) {
					order.setC_BPartner_ID(businessPartner.get_ID());
				}
			}

			// Set properties specifically for sales orders
			if (order.isSOTrx()) {
				ModelUtil.setPropertyIfPresent(entity.getbh_lab_notes(), order::setbh_lab_notes);
				ModelUtil.setPropertyIfPresent(entity.getDescription(), order::setDescription);

				if (entity.getPatientType() != null && !StringUtil.isNullOrEmpty(entity.getPatientType().getValue())) {
					ModelUtil.setPropertyIfPresent(entity.getPatientType().getValue(), order::setBH_PatientType);
				}
				if (entity.getReferral() != null && !StringUtil.isNullOrEmpty(entity.getReferral().getValue())) {
					ModelUtil.setPropertyIfPresent(entity.getReferral().getValue(), order::setbh_referral);
				}

				ModelUtil.setPropertyIfPresent(entity.isBH_NewVisit(), order::setBH_NewVisit);
				ModelUtil.setPropertyIfPresent(entity.getBH_Chief_Complaint(), order::setBH_Chief_Complaint);
				ModelUtil.setPropertyIfPresent(entity.getBH_Temperature(), order::setBH_Temperature);
				ModelUtil.setPropertyIfPresent(entity.getBH_Pulse(), order::setBH_Pulse);
				ModelUtil.setPropertyIfPresent(entity.getBH_Respiratory_Rate(), order::setBH_Respiratory_Rate);
				ModelUtil.setPropertyIfPresent(entity.getBH_Blood_Pressure(), order::setBH_Blood_Pressure);
				ModelUtil.setPropertyIfPresent(entity.getBH_Height(), order::setBH_Height);
				ModelUtil.setPropertyIfPresent(entity.getBH_Weight(), order::setBH_Weight);
				ModelUtil.setPropertyIfPresent(entity.getBH_SecondDiagnosis(), order::setBH_SecondDiagnosis);
				ModelUtil.setPropertyIfPresent(entity.isSOTrx(), order::setIsSOTrx);
			}

			// set target document type
			if (!order.isSOTrx()) {
				order.setC_DocTypeTarget_ID(getPurchaseOrderDocumentTypeId());
			}

			order.saveEx();

			List<String> orderLineUuidsToKeep = new ArrayList<>();
			// persist product/service/charge order lines
			List<OrderLineInput> orderLines = entity.getOrderLines();
			if (orderLines != null && !orderLines.isEmpty()) {
				for (OrderLineInput orderLine : orderLines) {
					orderLine.setC_Order_ID(order.get_ID());
					orderLineUuidsToKeep.add(orderLineRepository.save(orderLine).getC_OrderLine_UU());
				}
			}

			// delete order lines not in request
			orderLineRepository.deleteByOrder(order.get_ID(), orderLineUuidsToKeep);

			// Handle payments for sales orders
			if (order.isSOTrx()) {
				// any post save operation
				List<String> paymentUuidsToKeep = new ArrayList<>();
				List<PaymentInput> payments = entity.getPayments();
				if (payments != null && entity.isSOTrx()) {
					for (PaymentInput payment : entity.getPayments()) {
						payment.setC_Order_ID(order.get_ID());
						if (businessPartner != null) {
							payment.setC_BPartner_ID(businessPartner.getC_BPartner_ID());
						}
						paymentUuidsToKeep.add(paymentRepository.save(payment).getC_Payment_UU());
					}
				}

				// delete payment lines not in request
				paymentRepository.deleteByOrder(order.get_ID(), paymentUuidsToKeep);
			}

			cache.delete(order.get_ID());

			return getByUuid(order.getC_Order_UU());

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());

			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	public MOrder_BH saveSalesOrder(OrderInput entity) {
		entity.setIsSOTrx(true);
		return save(entity);
	}

	public MOrder_BH savePurchaseOrder(OrderInput entity) {
		entity.setIsSOTrx(false);
		return save(entity);
	}

	/**
	 * Process order
	 *
	 * @param uuid
	 * @return
	 */
	public CompletableFuture<MOrder_BH> process(String uuid) {
		MOrder_BH order = getByUuid(uuid);
		if (order == null) {
			logger.severe("No order with uuid = " + uuid);
			return null;
		}

		processRepository.runOrderProcess(order.get_ID());
		cache.delete(order.get_ID());
		businessPartnerRepository.cache.delete(order.getC_BPartner_ID());

		return CompletableFuture.supplyAsync(() -> getByUuid(order.getC_Order_UU()));
	}

	/**
	 * Get Purchase Order Target Document Type
	 *
	 * @return
	 */
	protected int getPurchaseOrderDocumentTypeId() {
		// set target document type
		MDocType docType = new Query(Env.getCtx(), MDocType.Table_Name,
				MDocType.COLUMNNAME_Name + "=? AND " + MDocType.COLUMNNAME_DocBaseType + "=?", null)
				.setParameters(PURCHASE_ORDER, MDocType.DOCBASETYPE_PurchaseOrder).setClient_ID().first();
		if (docType != null) {
			return docType.get_ID();
		}

		return 0;
	}
}
