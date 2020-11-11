package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.InvoiceLineInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceLineRepository;
import org.compiere.model.MInvoiceLine;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

public class InvoiceLineDataLoader extends BaseDataLoader<MInvoiceLine, InvoiceLineInput, InvoiceLineRepository>
		implements DataLoaderRegisterer {
	public static String INVOICE_LINE_BY_ID_DATA_LOADER = "invoiceLineByIdDataLoader";
	public static String INVOICE_LINE_BY_UUID_DATA_LOADER = "invoiceLineByUuidDataLoader";
	public static String INVOICE_LINE_BY_INVOICE_DATA_LOADER = "invoiceLineByInvoiceDataLoader";
	private final InvoiceLineRepository invoiceLineRepository;

	public InvoiceLineDataLoader() {
		invoiceLineRepository = new InvoiceLineRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return INVOICE_LINE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return INVOICE_LINE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected InvoiceLineRepository getRepositoryInstance() {
		return invoiceLineRepository;
	}

	@Override
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(INVOICE_LINE_BY_INVOICE_DATA_LOADER, DataLoader.newMappedDataLoader(getByInvoiceBatchLoader(),
				getOptionsWithCache(idempiereContext)));
	}

	private MappedBatchLoaderWithContext<String, List<MInvoiceLine>> getByInvoiceBatchLoader() {
		return (keys, batchLoaderEnvironment) -> invoiceLineRepository
				.getGroupsByIdsCompletableFuture(MInvoiceLine::getC_Invoice_ID, MInvoiceLine.COLUMNNAME_C_Invoice_ID, keys,
						batchLoaderEnvironment.getContext());
	}
}
