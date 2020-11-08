package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.input.OrderLineInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineRepository extends BaseRepository<MOrderLine_BH, OrderLineInput> {

	private final ProductRepository productRepository;
	private final ChargeRepository chargeRepository;

	public OrderLineRepository() {
		productRepository = new ProductRepository();
		chargeRepository = new ChargeRepository();
	}

	@Override
	protected MOrderLine_BH createModelInstance() {
		return new MOrderLine_BH(Env.getCtx(), 0, null);
	}

	public void deleteByOrder(int orderId, List<String> orderLineUuidsToKeep) {
		String whereClause = MOrderLine_BH.COLUMNNAME_C_Order_ID + "=?";
		if (orderLineUuidsToKeep != null && !orderLineUuidsToKeep.isEmpty()) {
			whereClause += " AND " + MOrderLine_BH.COLUMNNAME_C_OrderLine_UU + " NOT IN(" +
					orderLineUuidsToKeep.stream().map(orderLineUuidToKeep -> "'" + orderLineUuidToKeep + "'")
							.collect(Collectors.joining(",")) + ")";
		}

		List<MOrderLine_BH> orderLines = new Query(Env.getCtx(), MOrderLine_BH.Table_Name, whereClause, null)
				.setParameters(orderId).setClient_ID().list();
		orderLines.forEach(orderLine -> {
			orderLine.deleteEx(false);
		});
	}

	@Override
	public MOrderLine_BH mapInputModelToModel(OrderLineInput entity) {
		MOrderLine_BH orderLine = getByUuid(entity.getC_OrderLine_UU());
		if (orderLine == null) {
			orderLine = new MOrderLine_BH(Env.getCtx(), 0, null);
			orderLine.setAD_Org_ID(Env.getAD_Org_ID(Env.getCtx()));
		}

		ModelUtil.setPropertyIfPresent(entity.getC_Order_ID(), orderLine::setC_Order_ID);

		if (entity.getCharge() != null) {
			MCharge_BH charge = chargeRepository.getByUuid(entity.getCharge().getC_Charge_UU());

			if (charge != null) {
				orderLine.setC_Charge_ID(charge.get_ID());
			}
		}

		if (entity.getProduct() != null) {
			MProduct_BH product = productRepository.getByUuid(entity.getProduct().getM_Product_UU());

			if (product != null) {
				orderLine.setM_Product_ID(product.get_ID());
			}
		}

		ModelUtil.setPropertyIfPresent(entity.getPriceActual(), orderLine::setPriceActual);
		ModelUtil.setPropertyIfPresent(entity.getQtyOrdered(), orderLine::setQtyOrdered);
		// only save for receive products
		ModelUtil.setPropertyIfPresent(entity.getLineNetAmt(), orderLine::setLineNetAmt);
		ModelUtil.setPropertyIfPresent(entity.getM_AttributeSetInstance_ID(), orderLine::setM_AttributeSetInstance_ID);
		ModelUtil.setPropertyIfPresent(entity.getBH_Expiration(), orderLine::setBH_Expiration);
		ModelUtil.setPropertyIfPresent(entity.getBH_Instructions(), orderLine::setBH_Instructions);
		ModelUtil.setPropertyIfPresent(entity.isActive(), orderLine::setIsActive);

		return orderLine;
	}
}
