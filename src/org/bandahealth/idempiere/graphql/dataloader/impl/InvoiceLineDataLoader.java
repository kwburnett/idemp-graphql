package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.InvoiceLineInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceLineRepository;
import org.compiere.model.MInvoiceLine;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class InvoiceLineDataLoader extends BaseDataLoader<MInvoiceLine, InvoiceLineInput, InvoiceLineRepository>
		implements DataLoaderRegisterer {

	public static String INVOICE_LINE_DATA_LOADER = "invoiceLineDataLoader";
	public static String INVOICE_LINE_BY_INVOICE_DATA_LOADER = "invoiceLineByInvoiceDataLoader";
	private final InvoiceLineRepository invoiceLineRepository;

	public InvoiceLineDataLoader() {
		invoiceLineRepository = new InvoiceLineRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return INVOICE_LINE_DATA_LOADER;
	}

	@Override
	protected InvoiceLineRepository getRepositoryInstance() {
		return invoiceLineRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(INVOICE_LINE_BY_INVOICE_DATA_LOADER, DataLoader.newMappedDataLoader(getByInvoiceBatchLoader(),
				getOptionsWithCache()));
	}

	private MappedBatchLoader<Integer, List<MInvoiceLine>> getByInvoiceBatchLoader() {
		return keys -> invoiceLineRepository.getGroupsByIdsCompletableFuture(MInvoiceLine::getC_Invoice_ID,
				MInvoiceLine.COLUMNNAME_C_Invoice_ID, keys);
	}
}
