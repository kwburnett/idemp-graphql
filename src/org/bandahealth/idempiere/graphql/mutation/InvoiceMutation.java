package org.bandahealth.idempiere.graphql.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.base.model.MOrder_BH;
import org.bandahealth.idempiere.graphql.model.input.InvoiceInput;
import org.bandahealth.idempiere.graphql.model.input.OrderInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceRepository;

import java.util.concurrent.CompletableFuture;

public class InvoiceMutation implements GraphQLMutationResolver {

	private final InvoiceRepository invoiceRepository;

	public InvoiceMutation() {
		invoiceRepository = new InvoiceRepository();
	}

	public CompletableFuture<MInvoice_BH> saveCustomerInvoice(InvoiceInput invoice, boolean shouldProcess) {
		MInvoice_BH savedOrder = invoiceRepository.saveCustomerInvoice(invoice);
		if (shouldProcess) {
			return invoiceRepository.process(savedOrder.getC_Invoice_UU());
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MInvoice_BH> saveVendorInvoice(InvoiceInput invoice, boolean shouldProcess) {
		MInvoice_BH savedOrder = invoiceRepository.saveVendorInvoice(invoice);
		if (shouldProcess) {
			return invoiceRepository.process(savedOrder.getC_Invoice_UU());
		}
		return CompletableFuture.supplyAsync(() -> savedOrder);
	}

	public CompletableFuture<MInvoice_BH> processInvoice(String id) {
		return invoiceRepository.process(id);
	}

	public CompletableFuture<Boolean> deleteInvoice(String id) {
		return invoiceRepository.delete(id);
	}
}
