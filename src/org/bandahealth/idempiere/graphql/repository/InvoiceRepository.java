package org.bandahealth.idempiere.graphql.repository;

import graphql.schema.DataFetchingEnvironment;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.model.MBPartner_BH;
import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.model.input.InvoiceInput;
import org.bandahealth.idempiere.graphql.model.input.InvoiceLineInput;
import org.bandahealth.idempiere.graphql.utils.ModelUtil;
import org.compiere.model.MDocType;
import org.compiere.model.Query;
import org.compiere.util.CLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class InvoiceRepository extends BaseRepository<MInvoice_BH, InvoiceInput> {

	private final CLogger logger = CLogger.getCLogger(InvoiceRepository.class);

	private final InvoiceLineRepository invoiceLineRepository;
	private final BusinessPartnerRepository businessPartnerRepository;
	private final ProcessRepository processRepository;

	private final String businessPartnerJoin = "JOIN " + MBPartner_BH.Table_Name + " ON " + MBPartner_BH.Table_Name +
			"." + MBPartner_BH.COLUMNNAME_C_BPartner_ID + "=" + MInvoice_BH.Table_Name + "." +
			MInvoice_BH.COLUMNNAME_C_BPartner_ID;

	public InvoiceRepository() {
		invoiceLineRepository = new InvoiceLineRepository();
		businessPartnerRepository = new BusinessPartnerRepository();
		processRepository = new ProcessRepository();
	}

	public Connection<MInvoice_BH> getVendorInvoices(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("N");
		parameters.add("Y");
		parameters.add(MInvoice_BH.DOCSTATUS_Reversed);

		return super.get(filter, sort, pagingInfo, MInvoice_BH.COLUMNNAME_IsSOTrx + "=? AND " +
				MInvoice_BH.COLUMNNAME_BH_IsExpense + "=? AND " + MInvoice_BH.COLUMNNAME_DocStatus + "!=?", parameters,
				businessPartnerJoin, environment);
	}

	public Connection<MInvoice_BH> getCustomerInvoices(String filter, String sort, PagingInfo pagingInfo,
			DataFetchingEnvironment environment) {
		List<Object> parameters = new ArrayList<>();
		parameters.add("Y");
		parameters.add("N");
		parameters.add(MInvoice_BH.DOCSTATUS_Reversed);

		return super.get(filter, sort, pagingInfo, MInvoice_BH.COLUMNNAME_IsSOTrx + "=? AND " +
						MInvoice_BH.COLUMNNAME_BH_IsExpense + "=? AND " + MInvoice_BH.COLUMNNAME_DocStatus + "!=?", parameters,
				businessPartnerJoin, environment);
	}

	@Override
	protected MInvoice_BH createModelInstance(Properties idempiereContext) {
		return new MInvoice_BH(idempiereContext, 0, null);
	}

	@Override
	public MInvoice_BH mapInputModelToModel(InvoiceInput entity, Properties idempiereContext) {
		try {
			MInvoice_BH invoice = getByUuid(entity.getC_Invoice_UU(), idempiereContext);
			if (invoice == null) {
				invoice = createModelInstance(idempiereContext);
			}

			ModelUtil.setPropertyIfPresent(entity.getDateInvoiced(), invoice::setDateInvoiced);
			ModelUtil.setPropertyIfPresent(entity.getDescription(), invoice::setDescription);

			invoice.setIsActive(entity.isActive());
			invoice.setIsSOTrx(entity.isSOTrx());
			invoice.setIsApproved(true);
			invoice.setDocAction(MInvoice_BH.DOCACTION_Complete);
			invoice.setPaymentRule(entity.getPaymentRule());

			MBPartner_BH businessPartner = null;

			// set vendor
			if (entity.getBusinessPartner() != null && entity.getBusinessPartner().getC_BPartner_UU() != null) {
				businessPartner = businessPartnerRepository
						.getByUuid(entity.getBusinessPartner().getC_BPartner_UU(), idempiereContext);
				if (businessPartner != null) {
					invoice.setC_BPartner_ID(businessPartner.get_ID());
				}
			}

			// Set properties specifically for sales invoices
			if (!invoice.isSOTrx()) {
				int apInvoiceId = getAPInvoiceDocumentTypeId(idempiereContext);
				invoice.setC_DocType_ID(apInvoiceId);
				invoice.setC_DocTypeTarget_ID(apInvoiceId);
			}

			return invoice;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());

			throw new AdempiereException(ex.getLocalizedMessage());
		}
	}

	@Override
	public MInvoice_BH afterSave(InvoiceInput inputEntity, MInvoice_BH entity, Properties idempiereContext) {
		List<String> invoiceLineUuidsToKeep = new ArrayList<>();
		// persist product/service/charge invoice lines
		List<InvoiceLineInput> invoiceLines = inputEntity.getInvoiceLines();
		if (invoiceLines != null && !invoiceLines.isEmpty()) {
			for (InvoiceLineInput invoiceLine : invoiceLines) {
				invoiceLine.setC_Invoice_ID(entity.get_ID());
				invoiceLineUuidsToKeep.add(invoiceLineRepository.save(invoiceLine, idempiereContext).getC_InvoiceLine_UU());
			}
		}

		// delete invoice lines not in request
		invoiceLineRepository.deleteByInvoice(entity.get_ID(), invoiceLineUuidsToKeep, idempiereContext);

		return entity;
	}

	public MInvoice_BH saveCustomerInvoice(InvoiceInput entity, Properties idempiereContext) {
		entity.setIsSOTrx(true);
		entity.setBH_IsExpense(false);
		return save(entity, idempiereContext);
	}

	public MInvoice_BH saveVendorInvoice(InvoiceInput entity, Properties idempiereContext) {
		entity.setIsSOTrx(false);
		entity.setBH_IsExpense(true);
		return save(entity, idempiereContext);
	}

	/**
	 * Process invoice
	 *
	 * @param uuid
	 * @return
	 */
	public CompletableFuture<MInvoice_BH> process(String uuid, Properties idempiereContext) {
		MInvoice_BH invoice = getByUuid(uuid, idempiereContext);
		if (invoice == null) {
			logger.severe("No invoice with uuid = " + uuid);
			return null;
		}

		processRepository.runExpenseProcess(invoice.get_ID(), false, idempiereContext);
		cache.delete(invoice.get_ID());
		cache.delete(uuid);
		businessPartnerRepository.cache.delete(invoice.getC_BPartner_ID());

		return CompletableFuture.supplyAsync(() -> getByUuid(invoice.getC_Invoice_UU(), idempiereContext));
	}

	public CompletableFuture<Boolean> delete(String uuid, Properties idempiereContext) {
		MInvoice_BH invoice = getByUuid(uuid, idempiereContext);
		if (invoice == null) {
			logger.severe("No invoice with uuid = " + uuid);
			return CompletableFuture.supplyAsync(() -> false);
		}

		try {
			if (invoice.isComplete()) {
				processRepository.runExpenseProcess(invoice.get_ID(), false, idempiereContext);
			} else {
				invoice.deleteEx(false);
			}
			cache.delete(invoice.get_ID());
			businessPartnerRepository.cache.delete(invoice.getC_BPartner_ID());
			return CompletableFuture.supplyAsync(() -> true);
		} catch (Exception ex) {
			logger.severe("Delete exception: " + ex.getMessage());
		}

		return CompletableFuture.supplyAsync(() -> false);
	}

	/**
	 * Get the Invoice (Vendor) document type id
	 *
	 * @return
	 */
	protected int getAPInvoiceDocumentTypeId(Properties idempiereContext) {
		return new Query(idempiereContext, MDocType.Table_Name, MDocType.COLUMNNAME_DocBaseType + "=?",
				null)
				.setClient_ID()
				.setParameters(MDocType.DOCBASETYPE_APInvoice)
				.firstId();
	}
}
