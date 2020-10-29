package org.bandahealth.idempiere.graphql.model.input;

import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

public class InvoiceInput extends MInvoice_BH {

	private BusinessPartnerInput businessPartner;
	private List<InvoiceLineInput> invoiceLines;
	private ReferenceListInput paymentType;

	public InvoiceInput() {
		super(Env.getCtx(), 0, null);
	}

	public InvoiceInput(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
	}

	public InvoiceInput(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public InvoiceInput(MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate) {
		super(order, C_DocTypeTarget_ID, invoiceDate);
	}

	public InvoiceInput(MInvoice invoice) {
		super(invoice);
	}

	public void setId(String id) {
		this.set_Value(this.getUUIDColumnName(), id);
	}

	public void setBusinessPartner(BusinessPartnerInput businessPartner) {
		this.businessPartner = businessPartner;
	}

	public BusinessPartnerInput getBusinessPartner() {
		return businessPartner;
	}

	public void setInvoiceLines(List<InvoiceLineInput> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}

	public List<InvoiceLineInput> getInvoiceLines() {
		return invoiceLines;
	}

	public void setPaymentType(ReferenceListInput paymentType) {
		this.paymentType = paymentType;
		if (paymentType != null) {
			setPaymentRule(paymentType.getValue());
		}
	}

	public ReferenceListInput getPaymentType() {
		return paymentType;
	}

	public void setIsSalesTransaction(Boolean isSalesTransaction) {
		this.setIsSOTrx(isSalesTransaction);
	}

	public void setIsExpense(boolean isExpense) {
		this.setBH_IsExpense(isExpense);
	}
}
