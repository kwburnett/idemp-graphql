package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.utils.DateUtil;
import org.compiere.model.MOrder;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class OrderLineInput extends MOrderLine_BH {

	private ChargeInput charge;
	private ProductInput product;
	private AttributeSetInstanceInput attributeSetInstance;

	public OrderLineInput() {
		super(Env.getCtx(), 0, null);
	}

	public OrderLineInput(MOrder order) {
		super(order);
	}

	public OrderLineInput(Properties ctx, int C_OrderLine_ID, String trxName) {
		super(ctx, C_OrderLine_ID, trxName);
	}

	public OrderLineInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setCharge(ChargeInput charge) {
		this.charge = charge;
	}

	@Override
	public ChargeInput getCharge() {
		return charge;
	}

	public void setProduct(ProductInput product) {
		this.product = product;
	}

	@Override
	public ProductInput getProduct() {
		return product;
	}

	public void setAttributeSetInstance(AttributeSetInstanceInput attributeSetInstance) {
		this.attributeSetInstance = attributeSetInstance;
	}

	public AttributeSetInstanceInput getAttributeSetInstance() {
		return attributeSetInstance;
	}

	public void setPrice(BigDecimal price) {
		this.setPriceActual(price);
	}

	public void setQuantity(BigDecimal quantity) {
		this.setQtyOrdered(quantity);
	}

	public void setLineNetAmount(BigDecimal lineNetAmount) {
		this.setLineNetAmt(lineNetAmount);
	}

	public void setExpiration(String expiration) {
		this.setBH_Expiration(DateUtil.getTimestamp(expiration));
	}

	public void setInstructions(String instructions) {
		this.setBH_Instructions(instructions);
	}
}
