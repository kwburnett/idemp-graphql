package org.bandahealth.idempiere.graphql.context;

import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.bandahealth.idempiere.graphql.dataloader.*;
import org.compiere.util.CLogger;
import org.dataloader.DataLoaderRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

/**
 * Responsible for building the Banda-specific context and initializing any needed data to be made available on the
 * context.
 */
public class BandaGraphQLContextBuilder implements GraphQLServletContextBuilder {

	private final BandaDataLoaderComposer bandaDataLoaderComposer;
	CLogger logger = CLogger.getCLogger(BandaGraphQLContextBuilder.class);

	/**
	 * Called each request to create a context constructor to be used by requests (called in requests via
	 * `(DataFetchingEnvironment) environment.getContext()`).
	 */
	public BandaGraphQLContextBuilder() {
		logger.warning("Constructing GraphQL Context");
		bandaDataLoaderComposer = new BandaDataLoaderComposer();
	}

	/**
	 * By the time this context builder is invoked, the user is already authorized due to the filter. This is the
	 * constructor used for GET/POST requests to GraphQL.
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

	/**
	 * This is the constructor used for Subscriptions, of which there aren't currently any. If any are needed,
	 * this method can be updated to construct a context for those sessions.
	 *
	 * @param session          The session object passed by GraphQL.
	 * @param handshakeRequest The handshake request passed by GraphQL
	 * @return IllegalStateException - not implemented now
	 */
	@Override
	public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
		throw new IllegalStateException("Unavailable");
	}

	/**
	 * Not leveraged because, if any app-specific properties are needed on the context, they cannot be read from the
	 * request. However, if we're leveraging iDempiere's Env.ctx, we could pull from there without data from the request.
	 *
	 * @return IllegalStateException - not implemented now
	 */
	@Override
	public GraphQLContext build() {
		throw new IllegalStateException("Unavailable");
	}

	/**
	 * This is where all data loaders are added to the registry for this query. They are added per query, though
	 * the cache can be used to persist data/storage between queries.
	 *
	 * @return The DataLoaderRegistry for this request.
	 */
	private DataLoaderRegistry buildDataLoaderRegistry() {
		logger.warning("building data registry");
		DataLoaderRegistry registry = new DataLoaderRegistry();
		bandaDataLoaderComposer.addDataLoaders(registry);
		return registry;
	}
}
