package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.model.input.OrderLineInput;
import org.bandahealth.idempiere.graphql.model.input.PaymentInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class OrderRepository extends BaseRepository<MOrder_BH, OrderInput> {

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

	@Override
	public void updateCacheAfterSave(MOrder_BH entity, Properties idempiereContext) {
		super.updateCacheAfterSave(entity, idempiereContext);
		businessPartnerRepository.updateCacheAfterSave(businessPartnerRepository.getById(entity.getC_BPartner_ID(),
				idempiereContext), idempiereContext);
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
	protected MOrder_BH createModelInstance(Properties idempiereContext) {
		return new MOrder_BH(idempiereContext, 0, null);
	}

	@Override
	public MOrder_BH mapInputModelToModel(OrderInput entity, Properties idempiereContext) {
		try {
			MOrder_BH order = getByUuid(entity.getC_Order_UU(), idempiereContext);
			if (order == null) {
				order = createModelInstance(idempiereContext);
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
						.getByUuid(entity.getBusinessPartner().getC_BPartner_UU(), idempiereContext);
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
				order.setC_DocTypeTarget_ID(getPurchaseOrderDocumentTypeId(idempiereContext));
			}

			return order;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());

			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	@Override
	public MOrder_BH afterSave(OrderInput inputEntity, MOrder_BH entity, Properties idempiereContext) {
		List<String> orderLineUuidsToKeep = new ArrayList<>();
		// persist product/service/charge order lines
		List<OrderLineInput> orderLines = inputEntity.getOrderLines();
		if (orderLines != null && !orderLines.isEmpty()) {
			for (OrderLineInput orderLine : orderLines) {
				orderLine.setC_Order_ID(entity.get_ID());
				orderLineUuidsToKeep.add(orderLineRepository.save(orderLine, idempiereContext).getC_OrderLine_UU());
			}
		}

		// delete order lines not in request
		orderLineRepository.deleteByOrder(entity.get_ID(), orderLineUuidsToKeep, idempiereContext);

		// Handle payments for sales orders
		if (entity.isSOTrx()) {
			// any post save operation
			List<String> paymentUuidsToKeep = new ArrayList<>();
			List<PaymentInput> payments = inputEntity.getPayments();
			if (payments != null && inputEntity.isSOTrx()) {
				for (PaymentInput payment : inputEntity.getPayments()) {
					payment.setC_Order_ID(entity.get_ID());
					if (entity.getC_BPartner_ID() > 0) {
						payment.setC_BPartner_ID(entity.getC_BPartner_ID());
					}
					paymentUuidsToKeep.add(paymentRepository.save(payment, idempiereContext).getC_Payment_UU());
				}
			}

			// delete payment lines not in request
			paymentRepository.deleteByOrder(entity.get_ID(), paymentUuidsToKeep, idempiereContext);
		}

		return entity;
	}

	public MOrder_BH saveSalesOrder(OrderInput entity, Properties idempiereContext) {
		entity.setIsSOTrx(true);
		return save(entity, idempiereContext);
	}

	public MOrder_BH savePurchaseOrder(OrderInput entity, Properties idempiereContext) {
		entity.setIsSOTrx(false);
		return save(entity, idempiereContext);
	}

	/**
	 * Process order
	 *
	 * @param uuid
	 * @return
	 */
	public CompletableFuture<MOrder_BH> process(String uuid, Properties idempiereContext) {
		MOrder_BH order = getByUuid(uuid, idempiereContext);
		if (order == null) {
			logger.severe("No order with uuid = " + uuid);
			return null;
		}

		processRepository.runOrderProcess(order.get_ID(), idempiereContext);
		cache.delete(order.get_ID());
		cache.delete(uuid);
		businessPartnerRepository.cache.delete(order.getC_BPartner_ID());

		return CompletableFuture.supplyAsync(() -> getByUuid(order.getC_Order_UU(), idempiereContext));
	}

	/**
	 * Get Purchase Order Target Document Type
	 *
	 * @return
	 */
	protected int getPurchaseOrderDocumentTypeId(Properties idempiereContext) {
		// set target document type
		MDocType docType = new Query(idempiereContext, MDocType.Table_Name,
				MDocType.COLUMNNAME_Name + "=? AND " + MDocType.COLUMNNAME_DocBaseType + "=?", null)
				.setParameters(PURCHASE_ORDER, MDocType.DOCBASETYPE_PurchaseOrder).setClient_ID().first();
		if (docType != null) {
			return docType.get_ID();
		}

		return 0;
	}

	public CompletableFuture<Boolean> delete(String id, Properties idempiereContext) {
		try {
			MOrder order = getByUuid(id, idempiereContext);
			if (order.isComplete()) {
				throw new AdempiereException("Order is already completed");
			} else {
				return CompletableFuture.supplyAsync(() -> order.delete(false));
			}
		} catch (Exception ex) {
			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}
}
