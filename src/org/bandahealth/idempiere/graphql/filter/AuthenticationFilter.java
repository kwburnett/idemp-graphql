package org.bandahealth.idempiere.graphql.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private final List<String> ALLOWABLE_UNAUTHENTICATED_QUERIES = new ArrayList<String>() {{
		add("signIn");
	}};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
				AuthenticationUtil.validate(authHeaderVal.split(" ")[1]);
				if (Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_USER_ID))
						|| Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_ROLE_ID))) {
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
				paths.add("\"" + requestQuery.replace("mutation", "")
						.replace(":", " ").replace("(", " ").replace("{", " ")
						.split(" ")[1] + "\"");
			} catch (Exception ignore) {
			}
		}
		response.setContentType("application/json");
		String errorResponse = "{\"data\":{";
		if (!paths.isEmpty()) {
			errorResponse += paths.stream().reduce("", (accumulator, path) -> accumulator + path + ": null,");
		}
		errorResponse += "},\"errors\":[{\"message\": \"" + errorMessage + "\"";
		if (!paths.isEmpty()) {
			errorResponse += ",\"paths\":[" + String.join(",", paths) + "]";
		}
		errorResponse += "}}";
		response.getWriter().write(errorResponse);
	}
}
