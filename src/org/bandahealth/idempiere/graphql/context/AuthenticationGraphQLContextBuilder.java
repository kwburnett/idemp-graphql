package org.bandahealth.idempiere.graphql.context;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.adempiere.util.ServerContext;
import org.bandahealth.idempiere.graphql.utils.AuthenticationUtil;
import org.bandahealth.idempiere.graphql.utils.LoginClaims;
import org.bandahealth.idempiere.graphql.utils.TokenUtils;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;

public class AuthenticationGraphQLContextBuilder implements GraphQLServletContextBuilder {

	@Override
	public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String authHeaderVal = httpServletRequest.getHeader("Authorization");
		int userId = -1;
		// consume JWT i.e. execute signature validation
		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
			try {
				AuthenticationUtil.validate(authHeaderVal.split(" ")[1]);
				if (!Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_USER_ID))
						&& !Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_ROLE_ID))) {
					userId = Env.getAD_User_ID(Env.getCtx());
				}
			} catch (JWTVerificationException ex) {
//				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			} catch (Exception ignore) {
//				requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
			}
		} else {
//			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		DefaultGraphQLServletContext context = DefaultGraphQLServletContext.createServletContext()
				.with(httpServletRequest).with(httpServletResponse).build();
		return new AuthenticationGraphQLContext(context, userId);
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
