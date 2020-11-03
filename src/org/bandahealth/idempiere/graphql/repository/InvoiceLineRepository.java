package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.model.input.InvoiceLineInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceLineRepository extends BaseRepository<MInvoiceLine, InvoiceLineInput> {

	private final ProductRepository productRepository;
	private final ChargeRepository chargeRepository;

	public InvoiceLineRepository() {
		productRepository = new ProductRepository();
		chargeRepository = new ChargeRepository();
	}

	@Override
	public MInvoiceLine getModelInstance() {
		return new InvoiceLineInput();
	}

	public void deleteByInvoice(int invoiceId, List<String> invoiceLineUuidsToKeep) {
		String whereClause = MInvoiceLine.COLUMNNAME_C_Invoice_ID + "=?";
		if (invoiceLineUuidsToKeep != null && !invoiceLineUuidsToKeep.isEmpty()) {
			whereClause += " AND " + MInvoiceLine.COLUMNNAME_C_InvoiceLine_UU + " NOT IN(" +
					invoiceLineUuidsToKeep.stream().map(invoiceLineUuidToKeep -> "'" + invoiceLineUuidToKeep + "'")
							.collect(Collectors.joining(",")) + ")";
		}

		List<MInvoiceLine> invoiceLines = getBaseQuery(whereClause, invoiceId).list();
		invoiceLines.forEach(invoiceLine -> {
			invoiceLine.deleteEx(false);
		});
	}

	@Override
	public MInvoiceLine mapInputModelToModel(InvoiceLineInput entity) {
		MInvoiceLine invoiceLine = getByUuid(entity.getC_InvoiceLine_UU());
		if (invoiceLine == null) {
			invoiceLine = new InvoiceLineInput();
			invoiceLine.setAD_Org_ID(Env.getAD_Org_ID(Env.getCtx()));
		}

		ModelUtil.setPropertyIfPresent(entity.getC_Invoice_ID(), invoiceLine::setC_Invoice_ID);

		if (entity.getCharge() != null) {
			MCharge_BH charge = chargeRepository.getByUuid(entity.getCharge().getC_Charge_UU());

			if (charge != null) {
				invoiceLine.setC_Charge_ID(charge.get_ID());
			}
		}

		if (entity.getProduct() != null) {
			MProduct_BH product = productRepository.getByUuid(entity.getProduct().getM_Product_UU());

			if (product != null) {
				invoiceLine.setM_Product_ID(product.get_ID());
			}
		}

		ModelUtil.setPropertyIfPresent(entity.getPriceActual(), invoiceLine::setPriceActual);
		ModelUtil.setPropertyIfPresent(entity.getQtyInvoiced(), invoiceLine::setQty);
		// only save for receive products
		ModelUtil.setPropertyIfPresent(entity.getLineNetAmt(), invoiceLine::setLineNetAmt);
		ModelUtil.setPropertyIfPresent(entity.getM_AttributeSetInstance_ID(), invoiceLine::setM_AttributeSetInstance_ID);
		ModelUtil.setPropertyIfPresent(entity.isActive(), invoiceLine::setIsActive);
		ModelUtil.setPropertyIfPresent(entity.getDescription(), invoiceLine::setDescription);

		return invoiceLine;
	}
}
