package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.MCharge_BH;
import org.bandahealth.idempiere.base.model.MInvoice_BH;
import org.bandahealth.idempiere.base.model.MProduct_BH;
import org.bandahealth.idempiere.graphql.dataloader.impl.AttributeSetInstanceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.InvoiceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.impl.ProductDataLoader;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MInvoiceLine;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class InvoiceLineResolver extends BaseResolver<MInvoiceLine> implements GraphQLResolver<MInvoiceLine> {

	public CompletableFuture<MCharge_BH> charge(MInvoiceLine entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MCharge_BH> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ChargeDataLoader.CHARGE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getC_Charge_ID());
	}

	public CompletableFuture<MProduct_BH> product(MInvoiceLine entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MProduct_BH> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ProductDataLoader.PRODUCT_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_Product_ID());
	}

	public BigDecimal price(MInvoiceLine entity) {
		return entity.getPriceActual();
	}

	public BigDecimal quantity(MInvoiceLine entity) {
		return entity.getQtyInvoiced();
	}

	public BigDecimal lineNetAmount(MInvoiceLine entity) {
		return entity.getLineNetAmt();
	}

	public CompletableFuture<MInvoice_BH> invoice(MInvoiceLine entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MInvoice_BH> dataLoader =
				environment.getDataLoaderRegistry().getDataLoader(InvoiceDataLoader.INVOICE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getC_Invoice_ID());
	}

	public CompletableFuture<MAttributeSetInstance> attributeSetInstance(MInvoiceLine entity,
			DataFetchingEnvironment environment) {
		final DataLoader<Integer, MAttributeSetInstance> dataLoader = environment.getDataLoaderRegistry()
				.getDataLoader(AttributeSetInstanceDataLoader.ATTRIBUTE_SET_INSTANCE_BY_ID_DATA_LOADER);
		return dataLoader.load(entity.getM_AttributeSetInstance_ID());
	}
}
