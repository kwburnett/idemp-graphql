package org.bandahealth.idempiere.graphql.filter;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

import org.bandahealth.idempiere.graphql.utils.AuthenticationUtil;
import org.compiere.util.Env;
import org.compiere.util.Util;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Basic Authentication
 *
 * @author kevin
 */
public class AuthenticationFilter implements Filter {

	private final String ERROR_UNAUTHORIZED = "Unauthorized";
	private final String ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequestWrapper bandaRequest = new BandaServletRequestWrapper(request);
		String requestBody = bandaRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		// Don't filter a request to get an authentication session or to change a password
		if (bandaRequest.getMethod().equals(HttpMethod.POST) && (requestBody.contains("signIn("))) {
			chain.doFilter(bandaRequest, response);
			return;
		}

		String authHeaderVal = bandaRequest.getHeader(HttpHeaders.AUTHORIZATION);

		// consume JWT i.e. execute signature validation
		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
			try {
				AuthenticationUtil.validate(authHeaderVal.split(" ")[1]);
				if (Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_USER_ID))
						|| Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_ROLE_ID))) {
					abortRequest(response, ERROR_UNAUTHORIZED);
					return;
				}
			} catch (JWTVerificationException ex) {
				abortRequest(response, ERROR_UNAUTHORIZED);
				return;
			} catch (Exception ex) {
				abortRequest(response, ERROR_INTERNAL_SERVER_ERROR);
				return;
			}
		} else {
			abortRequest(response, ERROR_UNAUTHORIZED);
			return;
		}

		chain.doFilter(bandaRequest, response);
	}

	@Override
	public void destroy() { }

	private void abortRequest(ServletResponse response, String errorMessage) throws IOException {
		response.setContentType("application/json");
		response.getWriter().write("{ \"data\": {}, \"errors\": [ \"" + errorMessage + "\" ] }");
	}
}
