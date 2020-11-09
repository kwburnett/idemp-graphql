package org.bandahealth.idempiere.graphql.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * This class is meant to handle the input for invoice lines. The following properties are ignored because, when
 * the JSON parser tries to get to them (through MInvoiceLine), it gets incorrect mappings for MProduct, which will
 * throw a mapping exception for conflicting setters of MProduct.setResource. So, they are excluded (because they
 * currently aren't needed). To leverage them, define a custom getter/setter in this class to choose which models
 * to return.
 */
@JsonIgnoreProperties({"shipLine", "orderLine"})
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
