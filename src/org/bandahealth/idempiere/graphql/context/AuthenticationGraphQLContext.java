package org.bandahealth.idempiere.graphql.context;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import org.dataloader.DataLoaderRegistry;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AuthenticationGraphQLContext implements GraphQLServletContext {

	private final int userId;
	private final GraphQLServletContext context;

	public AuthenticationGraphQLContext(GraphQLServletContext context, int userId) {
		this.context = context;
		this.userId = userId;
	}

	public GraphQLServletContext getContext() {
		return context;
	}

	public int getUserId() {
		return userId;
	}

	@Override
	public List<Part> getFileParts() {
		return context.getFileParts();
	}

	@Override
	public Map<String, List<Part>> getParts() {
		return context.getParts();
	}

	@Override
	public HttpServletRequest getHttpServletRequest() {
		return context.getHttpServletRequest();
	}

	@Override
	public HttpServletResponse getHttpServletResponse() {
		return context.getHttpServletResponse();
	}

	@Override
	public Optional<Subject> getSubject() {
		return context.getSubject();
	}

	@Override
	public Optional<DataLoaderRegistry> getDataLoaderRegistry() {
		return context.getDataLoaderRegistry();
	}
}
