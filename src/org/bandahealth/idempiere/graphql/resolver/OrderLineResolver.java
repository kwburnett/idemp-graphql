package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.utils.DateUtil;

import java.math.BigDecimal;

public class OrderLineResolver extends BaseResolver<MOrderLine_BH> implements GraphQLResolver<MOrderLine_BH> {

	public MCharge_BH charge(MOrderLine_BH entity) {
		return (MCharge_BH) entity.getC_Charge();
	}

	public int orderId(MOrderLine_BH entity) {
		return entity.getC_Order_ID();
	}

	public MProduct_BH product(MOrderLine_BH entity) {
		return (MProduct_BH) entity.getProduct();
	}

	public BigDecimal price(MOrderLine_BH entity) {
		return entity.getPriceActual();
	}

	public BigDecimal quantity(MOrderLine_BH entity) {
		return entity.getQtyOrdered();
	}

	public BigDecimal lineNetAmount(MOrderLine_BH entity) {
		return entity.getLineNetAmt();
	}

	public int attributeSetInstanceId(MOrderLine_BH entity) {
		return entity.getM_AttributeSetInstance_ID();
	}

	public String expiration(MOrderLine_BH entity) {
		return DateUtil.parseDateOnly(entity.getBH_Expiration());
	}

	public String instructions(MOrderLine_BH entity) {
		return entity.getBH_Instructions();
	}
}
