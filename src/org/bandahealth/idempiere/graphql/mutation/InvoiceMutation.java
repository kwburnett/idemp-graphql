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

public class InvoiceMutation implements GraphQLMutationResolver {

	private final InvoiceRepository invoiceRepository;

	public InvoiceMutation() {
		invoiceRepository = new InvoiceRepository();
	}

	public CompletableFuture<MInvoice_BH> saveCustomerInvoice(InvoiceInput invoice, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MInvoice_BH savedInvoice = invoiceRepository.saveCustomerInvoice(invoice, idempiereContext);
		if (shouldProcess) {
			return invoiceRepository.process(savedInvoice.getC_Invoice_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedInvoice);
	}

	public CompletableFuture<MInvoice_BH> saveVendorInvoice(InvoiceInput invoice, boolean shouldProcess,
			DataFetchingEnvironment environment) {
		Properties idempiereContext = BandaGraphQLContext.getCtx(environment);
		MInvoice_BH savedInvoice = invoiceRepository.saveVendorInvoice(invoice, idempiereContext);
		if (shouldProcess) {
			return invoiceRepository.process(savedInvoice.getC_Invoice_UU(), idempiereContext);
		}
		return CompletableFuture.supplyAsync(() -> savedInvoice);
	}

	public CompletableFuture<MInvoice_BH> processInvoice(String id, DataFetchingEnvironment environment) {
		return invoiceRepository.process(id, BandaGraphQLContext.getCtx(environment));
	}

	public CompletableFuture<Boolean> deleteInvoice(String id, DataFetchingEnvironment environment) {
		return invoiceRepository.delete(id, BandaGraphQLContext.getCtx(environment));
	}
}
