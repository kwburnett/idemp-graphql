package org.bandahealth.idempiere.graphql.context;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoaderRegistry;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * This is the specific context where we can add our own properties. This will be available to every resolver via
 * the DataFetchingEnvironment argument passed to each resolver as the last argument (from it, call
 * `environment.getContext()`).
 */
public class BandaGraphQLContext implements GraphQLServletContext {

	private final Properties idempiereContext;
	private final GraphQLServletContext context;

	/**
	 * Add any properties that should be available on the context. We may not need any if we leverage Env.getCtx()
	 * everywhere.
	 *
	 * @param context The GraphQLServletContext created each request, containing the response/request/session, data loader
	 *                registry, etc.
	 * @param userId  The user ID of the user making the request.
	 */
	public BandaGraphQLContext(GraphQLServletContext context, Properties idempiereContext) {
		this.context = context;
		this.idempiereContext = idempiereContext;
	}

	/**
	 * A shortcut method to get the needed context. This is needed because Env.getCtx() isn't thread-safe.
	 *
	 * @param environment The data fetching environment object passed to all GraphQL queries
	 * @return The iDempiere context
	 */
	public static Properties getCtx(DataFetchingEnvironment environment) {
		return ((BandaGraphQLContext) environment.getContext()).getIdempiereContext();
	}

	public GraphQLServletContext getContext() {
		return context;
	}

	public Properties getIdempiereContext() {
		return idempiereContext;
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
