package org.bandahealth.idempiere.graphql.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.graphql.model.Connection;
import org.bandahealth.idempiere.graphql.model.PagingInfo;
import org.bandahealth.idempiere.graphql.repository.InvoiceRepository;

import java.util.concurrent.CompletableFuture;

public class InvoiceQuery implements GraphQLQueryResolver {

	private final InvoiceRepository invoiceRepository;

	public InvoiceQuery() {
		invoiceRepository = new InvoiceRepository();
	}

	public CompletableFuture<Connection<MInvoice_BH>> customerInvoices(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return CompletableFuture.supplyAsync(() ->
				invoiceRepository.getCustomerInvoices(filter, sort, new PagingInfo(page, pageSize), environment));
	}

	public CompletableFuture<Connection<MInvoice_BH>> vendorInvoices(String filter, String sort, int page, int pageSize,
			DataFetchingEnvironment environment) {
		return CompletableFuture.supplyAsync(() ->
				invoiceRepository.getVendorInvoices(filter, sort, new PagingInfo(page, pageSize), environment));
	}
}
