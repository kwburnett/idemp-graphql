//package org.bandahealth.idempiere.graphql.filter;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.sql.Timestamp;
//import java.util.Properties;
//import javax.ws.rs.HttpMethod;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//
//import org.compiere.model.MAcctSchema;
//import org.compiere.model.MClientInfo;
//import org.compiere.model.MRole;
//import org.compiere.util.Env;
//import org.compiere.util.Util;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.Claim;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.auth0.jwt.interfaces.JWTVerifier;
//
//import org.adempiere.util.ServerContext;
//import org.bandahealth.idempiere.graphql.IRestConfigs;
//import org.bandahealth.idempiere.graphql.utils.LoginClaims;
//import org.bandahealth.idempiere.graphql.utils.TokenUtils;
//
///**
// * Basic Authentication
// *
// * @author andrew
// */
//@Provider
//public class AuthenticationFilter implements ContainerRequestFilter {
//
//	public static final String LOGIN_NAME = "#LoginName";
//
//	@Override
//	public void filter(ContainerRequestContext requestContext) throws IOException {
//		// Don't filter a request to get an authentication session or to change a password
//		if (requestContext.getMethod().equals(HttpMethod.POST)
//				&& (requestContext.getUriInfo().getPath().endsWith(IRestConfigs.AUTHENTICATION_SESSION_PATH) ||
//				requestContext.getUriInfo().getPath().endsWith(IRestConfigs.CHANGEPASSWORD_PATH))) {
//			return;
//		}
//
//		String authHeaderVal = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//
//		// consume JWT i.e. execute signature validation
//		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
//			try {
//				validate(authHeaderVal.split(" ")[1]);
//				if (Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_USER_ID))
//						|| Util.isEmpty(Env.getContext(Env.getCtx(), Env.AD_ROLE_ID))) {
//					if (!requestContext.getUriInfo().getPath().startsWith(IRestConfigs.AUTHENTICATION)) {
//						requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//					}
//				}
//			} catch (JWTVerificationException ex) {
//				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//			} catch (Exception ex) {
//				requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
//			}
//		} else {
//			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//		}
//	}
//
//	/**
//	 * Borrowed from
//	 * https://github.com/hengsin/idempiere-rest/blob/master/com.trekglobal.idempiere.rest.api/src/com/trekglobal/idempiere/rest/api/v1/auth/filter/RequestFilter.java#L99
//	 *
//	 * @param token
//	 * @throws IllegalArgumentException
//	 * @throws UnsupportedEncodingException
//	 */
//	private void validate(String token) throws IllegalArgumentException, UnsupportedEncodingException {
//		Algorithm algorithm = Algorithm.HMAC256(TokenUtils.getTokenSecret());
//		JWTVerifier verifier = JWT.require(algorithm).withIssuer(TokenUtils.getTokenIssuer()).build(); // Reusable
//		// verifier
//		// instance
//		DecodedJWT jwt = verifier.verify(token);
//		String userName = jwt.getSubject();
//		ServerContext.setCurrentInstance(new Properties());
//		Env.setContext(Env.getCtx(), LOGIN_NAME, userName);
//		Claim claim = jwt.getClaim(LoginClaims.AD_Client_ID.name());
//		int AD_Client_ID = 0;
//		if (!claim.isNull()) {
//			AD_Client_ID = claim.asInt();
//			Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, AD_Client_ID);
//		}
//		claim = jwt.getClaim(LoginClaims.AD_User_ID.name());
//		if (!claim.isNull()) {
//			Env.setContext(Env.getCtx(), Env.AD_USER_ID, claim.asInt());
//		}
//		claim = jwt.getClaim(LoginClaims.AD_Role_ID.name());
//		int AD_Role_ID = 0;
//		if (!claim.isNull()) {
//			AD_Role_ID = claim.asInt();
//			Env.setContext(Env.getCtx(), Env.AD_ROLE_ID, AD_Role_ID);
//		}
//		claim = jwt.getClaim(LoginClaims.AD_Org_ID.name());
//		int AD_Org_ID = 0;
//		if (!claim.isNull()) {
//			AD_Org_ID = claim.asInt();
//			Env.setContext(Env.getCtx(), Env.AD_ORG_ID, AD_Org_ID);
//		}
//		claim = jwt.getClaim(LoginClaims.M_Warehouse_ID.name());
//		if (!claim.isNull()) {
//			Env.setContext(Env.getCtx(), Env.M_WAREHOUSE_ID, claim.asInt());
//		}
//
//		if (AD_Role_ID > 0) {
//			if (MRole.getDefault(Env.getCtx(), false).isShowAcct())
//				Env.setContext(Env.getCtx(), "#ShowAcct", "Y");
//			else
//				Env.setContext(Env.getCtx(), "#ShowAcct", "N");
//		}
//
//		Env.setContext(Env.getCtx(), "#Date", new Timestamp(System.currentTimeMillis()));
//
//		/** Define AcctSchema , Currency, HasAlias **/
//		if (AD_Client_ID > 0) {
//			if (MClientInfo.get(Env.getCtx(), AD_Client_ID).getC_AcctSchema1_ID() > 0) {
//				MAcctSchema primary = MAcctSchema.get(Env.getCtx(),
//						MClientInfo.get(Env.getCtx(), AD_Client_ID).getC_AcctSchema1_ID());
//				Env.setContext(Env.getCtx(), "$C_AcctSchema_ID", primary.getC_AcctSchema_ID());
//				Env.setContext(Env.getCtx(), "$C_Currency_ID", primary.getC_Currency_ID());
//				Env.setContext(Env.getCtx(), "$HasAlias", primary.isHasAlias());
//			}
//
//			MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(Env.getCtx(), AD_Client_ID);
//			if (ass != null && ass.length > 1) {
//				for (MAcctSchema as : ass) {
//					if (as.getAD_OrgOnly_ID() != 0) {
//						if (as.isSkipOrg(AD_Org_ID)) {
//							continue;
//						} else {
//							Env.setContext(Env.getCtx(), "$C_AcctSchema_ID", as.getC_AcctSchema_ID());
//							Env.setContext(Env.getCtx(), "$C_Currency_ID", as.getC_Currency_ID());
//							Env.setContext(Env.getCtx(), "$HasAlias", as.isHasAlias());
//							break;
//						}
//					}
//				}
//			}
//		}
//	}
//}
