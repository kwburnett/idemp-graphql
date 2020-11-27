package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.context.BandaGraphQLContext;
import org.bandahealth.idempiere.graphql.model.input.InvoiceInput;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceRepository;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Handle all mutations relating to invoices
 */
public class InvoiceMutation implements GraphQLMutationResolver {
	private final InvoiceRepository invoiceRepository;

	public InvoiceMutation() {
		invoiceRepository = new InvoiceRepository();
	}

	/**
	 * Save information specific to a customer invoice, which is an invoice
	 *
	 * @param invoice       The information to save
	 * @param shouldProcess Whether the invoice should be processed after save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated invoice
	 */
	public CompletableFuture<MInvoice_BH> saveCustomerInvoice(InvoiceInput invoice, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MInvoice_BH savedInvoice = invoiceRepository.saveCustomerInvoice(invoice, idempiereContext);
		if (shouldProcess) {
			return invoiceRepository.process(savedInvoice.getC_Invoice_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedInvoice);
	}

	/**
	 * Save information specific to a vendor invoice, which is an invoice
	 *
	 * @param invoice       The information to save
	 * @param shouldProcess Whether the invoice should be processed after save
	 * @param environment   The environment associated with all calls, containing context
	 * @return The updated invoice
	 */
	public CompletableFuture<MInvoice_BH> saveVendorInvoice(InvoiceInput invoice, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MInvoice_BH savedInvoice = invoiceRepository.saveVendorInvoice(invoice, idempiereContext);
		if (shouldProcess) {
			return invoiceRepository.process(savedInvoice.getC_Invoice_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedInvoice);
	}

	/**
	 * Process the invoice without need to update beforehand
	 *
	 * @param id          The UUID of the invoice to process
	 * @param environment The environment associated with all calls, containing context
	 * @return The updated invoice
	 */
	public CompletableFuture<MInvoice_BH> processInvoice(String id, DataFetchingEnvironment environment) {
		return invoiceRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}

	/**
	 * Delete the specified invoice
	 *
	 * @param id          The UUID of the invoice to process
	 * @param environment The environment associated with all calls, containing context
	 * @return Whether the invoice was successfully deleted or not
	 */
	public CompletableFuture<Boolean> deleteInvoice(String id, DataFetchingEnvironment environment) {
		return invoiceRepository.delete(id, BandaGraphQLContext.getCtx(environment));
	}
}
