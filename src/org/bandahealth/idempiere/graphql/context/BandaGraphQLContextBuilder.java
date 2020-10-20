package org.bandahealth.idempiere.graphql.context;

import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.bandahealth.idempiere.base.model.MOrderLine_BH;
import org.bandahealth.idempiere.graphql.dataloader.OrderLineDataLoader;
import org.bandahealth.idempiere.graphql.dataloader.ReferenceListDataLoader;
import org.compiere.model.MRefList;
import org.compiere.util.CLogger;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.List;

public class BandaGraphQLContextBuilder implements GraphQLServletContextBuilder {

	CLogger logger = CLogger.getCLogger(BandaGraphQLContextBuilder.class);

	private final DataLoader<Integer, List<MOrderLine_BH>> orderLineDataLoader;
	private final DataLoader<String, MRefList> referenceListPatientTypeDataLoader;
	private final DataLoader<String, MRefList> referenceListReferralDataLoader;
	private final DataLoader<String, MRefList> referenceListOrderPaymentTypeDataLoader;
	private final DataLoader<String, MRefList> referenceListInvoicePaymentTypeDataLoader;

	public BandaGraphQLContextBuilder() {
		logger.warning("Constructing GraphQL Context");
		orderLineDataLoader = DataLoader.newMappedDataLoader(OrderLineDataLoader.getBatchLoader());
		referenceListPatientTypeDataLoader = DataLoader
				.newMappedDataLoader(ReferenceListDataLoader.getPatientTypeBatchLoader());
		referenceListReferralDataLoader = DataLoader.newMappedDataLoader(ReferenceListDataLoader.getReferralBatchLoader());
		referenceListOrderPaymentTypeDataLoader = DataLoader
				.newMappedDataLoader(ReferenceListDataLoader.getOrderPaymentTypeBatchLoader());
		referenceListInvoicePaymentTypeDataLoader = DataLoader
				.newMappedDataLoader(ReferenceListDataLoader.getInvoicePaymentTypeBatchLoader());
	}

	/**
	 * By the time this context builder is invoked, the user is already authorized due to the filter
	 *
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@Override
	public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String authHeaderVal = httpServletRequest.getHeader("Authorization");
		int userId = -1;
		DefaultGraphQLServletContext context = DefaultGraphQLServletContext.createServletContext()
				.with(httpServletRequest).with(httpServletResponse).with(buildDataLoaderRegistry()).build();
		return new BandaGraphQLContext(context, userId);
	}

	@Override
	public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
		throw new IllegalStateException("Unavailable");
	}

	@Override
	public GraphQLContext build() {
		throw new IllegalStateException("Unavailable");
	}

	private DataLoaderRegistry buildDataLoaderRegistry() {
		DataLoaderRegistry registry = new DataLoaderRegistry();
		registry.register(OrderLineDataLoader.ORDER_LINE_DATA_LOADER_NAME, orderLineDataLoader);
		registry.register(ReferenceListDataLoader.PATIENT_TYPE_DATA_LOADER_NAME, referenceListPatientTypeDataLoader);
		registry.register(ReferenceListDataLoader.REFERRAL_DATA_LOADER_NAME, referenceListReferralDataLoader);
		registry.register(ReferenceListDataLoader.ORDER_PAYMENT_TYPE_DATA_LOADER_NAME,
				referenceListOrderPaymentTypeDataLoader);
		registry.register(ReferenceListDataLoader.INVOICE_PAYMENT_TYPE_DATA_LOADER_NAME,
				referenceListInvoicePaymentTypeDataLoader);
		return registry;
	}
}
