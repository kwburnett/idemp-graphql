package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.*;
import org.bandahealth.idempiere.graphql.dataloader.impl.*;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MRefList;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class InvoiceResolver extends BaseResolver<MInvoice_BH> implements GraphQLResolver<MInvoice_BH> {
	public CompletableFuture<MBPartner_BH> businessPartner(MInvoice_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MBPartner_BH> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(BusinessPartnerDataLoader.BUSINESS_PARTNER_DATA_LOADER);
		return dataLoader.load(entity.getC_BPartner_ID());
	}

	public CompletableFuture<List<MInvoiceLine>> invoiceLines(MInvoice_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, List<MInvoiceLine>> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(InvoiceLineDataLoader.INVOICE_LINE_BY_INVOICE_DATA_LOADER);
		return dataLoader.load(entity.getC_Invoice_ID());
	}

	public boolean isSalesTransaction(MInvoice_BH entity) {
		return entity.isSOTrx();
	}

	public boolean isExpense(MInvoice_BH entity) {
		return entity.getBH_IsExpense();
	}

	public CompletableFuture<MRefList> paymentType(MInvoice_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<String, MRefList> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(ReferenceListDataLoader.INVOICE_PAYMENT_TYPE_DATA_LOADER);
		return dataLoader.load(entity.getPaymentRule());
	}
}
