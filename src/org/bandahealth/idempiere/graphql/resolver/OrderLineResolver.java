package org.bandahealth.idempiere.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.bandahealth.idempiere.base.model.*;
import org.bandahealth.idempiere.graphql.dataloader.AttributeSetInstanceDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.ChargeDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.OrderDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.ProductDataLoader;
import org.compiere.model.MAttributeSetInstance;
import org.dataloader.DataLoader;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

public class OrderLineResolver extends BaseResolver<MOrderLine_BH> implements GraphQLResolver<MOrderLine_BH> {

	public CompletableFuture<MCharge_BH> charge(MOrderLine_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MCharge_BH> chargeDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ChargeDataLoader.CHARGE_DATA_LOADER);
		return chargeDataLoader.load(entity.getC_Charge_ID());
	}

	public int orderId(MOrderLine_BH entity) {
		return entity.getC_Order_ID();
	}

	public CompletableFuture<MProduct_BH> product(MOrderLine_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MProduct_BH> productDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(ProductDataLoader.PRODUCT_DATA_LOADER);
		return productDataLoader.load(entity.getM_Product_ID());
	}

	public BigDecimal price(MOrderLine_BH entity) {
		return entity.getPriceActual();
	}

	public BigDecimal quantity(MOrderLine_BH entity) {
		return entity.getQtyOrdered();
	}

	public BigDecimal lineNetAmount(MOrderLine_BH entity) {
		return entity.getLineNetAmt();
	}

	public int attributeSetInstanceId(MOrderLine_BH entity) {
		return entity.getM_AttributeSetInstance_ID();
	}

	public Timestamp expiration(MOrderLine_BH entity) {
		return entity.getBH_Expiration();
	}

	public String instructions(MOrderLine_BH entity) {
		return entity.getBH_Instructions();
	}

	public CompletableFuture<MOrder_BH> order(MOrderLine_BH entity, DataFetchingEnvironment environment) {
		final DataLoader<Integer, MOrder_BH> orderDataLoader =
				environment.getDataLoaderRegistry().getDataLoader(OrderDataLoader.ORDER_DATA_LOADER);
		return orderDataLoader.load(entity.getC_Order_ID());
	}

	public CompletableFuture<MAttributeSetInstance> attributeSetInstance(MOrderLine_BH entity,
																																			 DataFetchingEnvironment environment) {
		final DataLoader<Integer, MAttributeSetInstance> attributeSetInstanceDataLoader =
				environment.getDataLoaderRegistry()
						.getDataLoader(AttributeSetInstanceDataLoader.ATTRIBUTE_SET_INSTANCE_DATA_LOADER);
		return attributeSetInstanceDataLoader.load(entity.getM_AttributeSetInstance_ID());
	}
}
