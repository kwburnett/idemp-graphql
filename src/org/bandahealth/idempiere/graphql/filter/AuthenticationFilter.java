package org.bandahealth.idempiere.graphql.filter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bandahealth.idempiere.graphql.utils.AuthenticationUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.util.Env;
import org.compiere.util.Util;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Basic Authentication on all requests
 *
 * @author kevin
 */
public class AuthenticationFilter implements Filter {

	private final String ERROR_UNAUTHORIZED = "Unauthorized";
	private final String ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error";
	/**
	 * These are the queries that can be used without authentication
	 */
	private final List<String> ALLOWABLE_UNAUTHENTICATED_QUERIES = new ArrayList<String>() {{
		add("signIn");
		add("changePassword");
	}};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Intentionally left blank
	}

	/**
	 * This performs the actual filtering of the requests
	 *
	 * @param request  The passed-in request
	 * @param response The response to leverage
	 * @param chain    The passed-in filter chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequestWrapper bandaRequest = new BandaServletRequestWrapper(request);
		String requestQuery = bandaRequest.getParameter("query");
		// If the query parameter didn't come through, try to pull it from the body
		if (StringUtil.isNullOrEmpty(requestQuery)) {
			try {
				String requestBody = bandaRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				requestQuery = (String) new ObjectMapper().readValue(requestBody, HashMap.class).get("query");
			} catch (Exception ignore) {
			}
			if (StringUtil.isNullOrEmpty(requestQuery)) {
				requestQuery = "";
			}
		}
		// Don't filter a request to get an authentication session or to change a password
		if (bandaRequest.getMethod().equals(HttpMethod.POST)) {
			boolean requestCanProceedWithoutAuthentication = false;
			for (String allowableUnauthenticatedQuery : ALLOWABLE_UNAUTHENTICATED_QUERIES) {
				if (requestQuery.contains(allowableUnauthenticatedQuery + "(")) {
					requestCanProceedWithoutAuthentication = true;
					break;
				}
			}
			if (requestCanProceedWithoutAuthentication) {
				chain.doFilter(bandaRequest, response);
				return;
			}
		}

		String authHeaderVal = bandaRequest.getHeader(HttpHeaders.AUTHORIZATION);

		// consume JWT i.e. execute signature validation
		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
			try {
				Properties idempiereContext = Env.getCtx();
				AuthenticationUtil.validate(authHeaderVal.split(" ")[1], idempiereContext);
				if (Util.isEmpty(Env.getContext(idempiereContext, Env.AD_USER_ID))
						|| Util.isEmpty(Env.getContext(idempiereContext, Env.AD_ROLE_ID))) {
					abortRequest(requestQuery, response, ERROR_UNAUTHORIZED);
					return;
				}
			} catch (JWTVerificationException ex) {
				abortRequest(requestQuery, response, ERROR_UNAUTHORIZED);
				return;
			} catch (Exception ex) {
				abortRequest(requestQuery, response, ERROR_INTERNAL_SERVER_ERROR);
				return;
			}
		} else {
			abortRequest(requestQuery, response, ERROR_UNAUTHORIZED);
			return;
		}

		chain.doFilter(bandaRequest, response);
	}

	@Override
	public void destroy() {
		// Intentionally left blank
	}

	/**
	 * Write a response body as close as possible to what GraphQL generates
	 *
	 * @param requestQuery The query string passed in as part of the request
	 * @param response     The response to return
	 * @param errorMessage The error message to use
	 * @throws IOException
	 */
	private void abortRequest(String requestQuery, ServletResponse response, String errorMessage) throws IOException {
		List<String> paths = new ArrayList<>();
		if (!StringUtil.isNullOrEmpty(requestQuery)) {
			try {
				// Try to get the requested queries so they can be returned in the error
				paths.add("\"" + Arrays.stream(StringUtil.stripNewLines(requestQuery).replace("mutation", "")
						.replace(":", " ").replace("(", " ").replace("{", " ")
						.replace("$", " ").replace("!", " ").replace("query", "")
						.split(" ")).filter(s -> !StringUtil.isNullOrEmpty(s)).findFirst().orElse("unknown") + "\"");
			} catch (Exception ignore) {
			}
		}
		response.setContentType("application/json");
		String errorResponse = "{\"data\":{";
		if (!paths.isEmpty()) {
			errorResponse += paths.stream().map(path -> path + ": null").collect(Collectors.joining(","));
		}
		errorResponse += "},\"errors\":[{\"message\": \"" + errorMessage + "\"";
		if (!paths.isEmpty()) {
			errorResponse += ",\"paths\":[" + String.join(",", paths) + "]";
		}
		errorResponse += "}]}";
		response.getWriter().write(errorResponse);
	}
}
