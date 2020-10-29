package org.bandahealth.idempiere.graphql.model.input;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class InvoiceLineInput extends MInvoiceLine {

	private ChargeInput charge;
	private ProductInput product;
	private AttributeSetInstanceInput attributeSetInstance;

	public InvoiceLineInput() {
		super(Env.getCtx(), 0, null);
	}

	public InvoiceLineInput(Properties ctx, int C_InvoiceLine_ID, String trxName) {
		super(ctx, C_InvoiceLine_ID, trxName);
	}

	public InvoiceLineInput(MInvoice invoice) {
		super(invoice);
	}

	public InvoiceLineInput(Properties ctx, ResultSet rs, String trxName) {
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
		this.setQty(quantity);
	}

	public void setLineNetAmount(BigDecimal lineNetAmount) {
		this.setLineNetAmt(lineNetAmount);
	}
}
