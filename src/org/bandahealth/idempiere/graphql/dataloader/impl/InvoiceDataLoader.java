package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.InvoiceInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

import java.util.List;

public class InvoiceDataLoader extends BaseDataLoader<MInvoice_BH, InvoiceInput, InvoiceRepository>
		implements DataLoaderRegisterer {
	public static String INVOICE_BY_ID_DATA_LOADER = "invoiceByIdDataLoader";
	public static String INVOICE_BY_UUID_DATA_LOADER = "invoiceByUuidDataLoader";
	public static String VENDOR_INVOICE_BY_BUSINESS_PARTNER_DATA_LOADER = "vendorInvoiceByBusinessPartnerDataLoader";
	private final InvoiceRepository invoiceRepository;

	public InvoiceDataLoader() {
		invoiceRepository = new InvoiceRepository();
	}

	@Override
	public void register(DataLoaderRegistry registry) {
		super.register(registry);
		registry.register(VENDOR_INVOICE_BY_BUSINESS_PARTNER_DATA_LOADER,
				DataLoader.newMappedDataLoader(getVendorInvoiceByBusinessPartnerBatchLoader(), getOptionsWithCache()));
	}

	@Override
	protected String getByIdDataLoaderName() {
		return INVOICE_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return INVOICE_BY_UUID_DATA_LOADER;
	}

	@Override
	protected InvoiceRepository getRepositoryInstance() {
		return invoiceRepository;
	}

	private MappedBatchLoader<String, List<MInvoice_BH>> getVendorInvoiceByBusinessPartnerBatchLoader() {
		return keys -> invoiceRepository.getGroupsByIdsCompletableFuture(MInvoice_BH::getC_BPartner_ID,
				MInvoice_BH.COLUMNNAME_C_BPartner_ID, keys);
	}
}
