package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.InvoiceInput;
import org.bandahealth.idempiere.graphql.repository.InvoiceRepository;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Properties;

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
	public void register(DataLoaderRegistry registry, Properties idempiereContext) {
		super.register(registry, idempiereContext);
		registry.register(VENDOR_INVOICE_BY_BUSINESS_PARTNER_DATA_LOADER,
				DataLoader.newMappedDataLoader(getVendorInvoiceByBusinessPartnerBatchLoader(),
						getOptionsWithCache(idempiereContext)));
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

	private MappedBatchLoaderWithContext<String, List<MInvoice_BH>> getVendorInvoiceByBusinessPartnerBatchLoader() {
		return (keys, batchLoaderEnvironment) -> invoiceRepository
				.getGroupsByIdsCompletableFuture(MInvoice_BH::getC_BPartner_ID, MInvoice_BH.COLUMNNAME_C_BPartner_ID, keys,
						batchLoaderEnvironment.getContext());
	}
}
