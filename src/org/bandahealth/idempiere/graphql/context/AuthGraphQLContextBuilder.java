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
import org.bandahealth.idempiere.graphql.utils.LoginClaims;
import org.bandahealth.idempiere.graphql.utils.TokenUtils;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.compiere.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;

public class AuthGraphQLContextBuilder implements GraphQLServletContextBuilder {

	public static final String LOGIN_NAME = "#LoginName";

	@Override
	public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String authHeaderVal = httpServletRequest.getHeader("Authorization");
		int userId = -1;
		// consume JWT i.e. execute signature validation
		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
			try {
				validate(authHeaderVal.split(" ")[1]);
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
		return new AuthGraphQLContext(context, userId);
	}

	@Override
	public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
		throw new IllegalStateException("Unavailable");
	}

	@Override
	public GraphQLContext build() {
		throw new IllegalStateException("Unavailable");
	}

	/**
	 * Borrowed from
	 * https://github.com/hengsin/idempiere-rest/blob/master/com.trekglobal.idempiere.rest.api/src/com/trekglobal/idempiere/rest/api/v1/auth/filter/RequestFilter.java#L99
	 *
	 * @param token
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	private void validate(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256(TokenUtils.getTokenSecret());
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(TokenUtils.getTokenIssuer()).build(); // Reusable
		// verifier
		// instance
		DecodedJWT jwt = verifier.verify(token);
		String userName = jwt.getSubject();
		ServerContext.setCurrentInstance(new Properties());
		Env.setContext(Env.getCtx(), LOGIN_NAME, userName);
		Claim claim = jwt.getClaim(LoginClaims.AD_Client_ID.name());
		int AD_Client_ID = 0;
		if (!claim.isNull()) {
			AD_Client_ID = claim.asInt();
			Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, AD_Client_ID);
		}
		claim = jwt.getClaim(LoginClaims.AD_User_ID.name());
		if (!claim.isNull()) {
			Env.setContext(Env.getCtx(), Env.AD_USER_ID, claim.asInt());
		}
		claim = jwt.getClaim(LoginClaims.AD_Role_ID.name());
		int AD_Role_ID = 0;
		if (!claim.isNull()) {
			AD_Role_ID = claim.asInt();
			Env.setContext(Env.getCtx(), Env.AD_ROLE_ID, AD_Role_ID);
		}
		claim = jwt.getClaim(LoginClaims.AD_Org_ID.name());
		int AD_Org_ID = 0;
		if (!claim.isNull()) {
			AD_Org_ID = claim.asInt();
			Env.setContext(Env.getCtx(), Env.AD_ORG_ID, AD_Org_ID);
		}
		claim = jwt.getClaim(LoginClaims.M_Warehouse_ID.name());
		if (!claim.isNull()) {
			Env.setContext(Env.getCtx(), Env.M_WAREHOUSE_ID, claim.asInt());
		}

		if (AD_Role_ID > 0) {
			if (MRole.getDefault(Env.getCtx(), false).isShowAcct())
				Env.setContext(Env.getCtx(), "#ShowAcct", "Y");
			else
				Env.setContext(Env.getCtx(), "#ShowAcct", "N");
		}

		Env.setContext(Env.getCtx(), "#Date", new Timestamp(System.currentTimeMillis()));

		/** Define AcctSchema , Currency, HasAlias **/
		if (AD_Client_ID > 0) {
			if (MClientInfo.get(Env.getCtx(), AD_Client_ID).getC_AcctSchema1_ID() > 0) {
				MAcctSchema primary = MAcctSchema.get(Env.getCtx(),
						MClientInfo.get(Env.getCtx(), AD_Client_ID).getC_AcctSchema1_ID());
				Env.setContext(Env.getCtx(), "$C_AcctSchema_ID", primary.getC_AcctSchema_ID());
				Env.setContext(Env.getCtx(), "$C_Currency_ID", primary.getC_Currency_ID());
				Env.setContext(Env.getCtx(), "$HasAlias", primary.isHasAlias());
			}

			MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(Env.getCtx(), AD_Client_ID);
			if (ass != null && ass.length > 1) {
				for (MAcctSchema as : ass) {
					if (as.getAD_OrgOnly_ID() != 0) {
						if (as.isSkipOrg(AD_Org_ID)) {
							continue;
						} else {
							Env.setContext(Env.getCtx(), "$C_AcctSchema_ID", as.getC_AcctSchema_ID());
							Env.setContext(Env.getCtx(), "$C_Currency_ID", as.getC_Currency_ID());
							Env.setContext(Env.getCtx(), "$HasAlias", as.isHasAlias());
							break;
						}
					}
				}
			}
		}
	}
}
