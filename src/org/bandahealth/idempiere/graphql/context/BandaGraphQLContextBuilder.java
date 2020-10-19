package org.bandahealth.idempiere.graphql.context;

import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.bandahealth.idempiere.graphql.dataloader.DataLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

public class BandaGraphQLContextBuilder implements GraphQLServletContextBuilder {

	/**
	 * By the time this context builder is invoked, the user is already authorized due to the filter
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@Override
	public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String authHeaderVal = httpServletRequest.getHeader("Authorization");
		int userId = -1;
		DefaultGraphQLServletContext context = DefaultGraphQLServletContext.createServletContext()
				.with(httpServletRequest).with(httpServletResponse).with(DataLoader.getRegistry()).build();
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
}
