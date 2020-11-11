package org.bandahealth.idempiere.graphql.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.adempiere.util.ServerContext;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MRole;
import org.compiere.util.Env;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;

public class AuthenticationUtil {

	public static final String LOGIN_NAME = "#LoginName";

	/**
	 * Borrowed from
	 * https://github.com/hengsin/idempiere-rest/blob/master/com.trekglobal.idempiere.rest.api/src/com/trekglobal/idempiere/rest/api/v1/auth/filter/RequestFilter.java#L99
	 *
	 * @param token
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public static void validate(String token, Properties context) throws IllegalArgumentException, UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256(TokenUtils.getTokenSecret());
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(TokenUtils.getTokenIssuer()).build(); // Reusable
		// verifier
		// instance
		DecodedJWT jwt = verifier.verify(token);
		String userName = jwt.getSubject();
		ServerContext.setCurrentInstance(new Properties());
		Env.setContext(context, LOGIN_NAME, userName);
		Claim claim = jwt.getClaim(LoginClaims.AD_Client_ID.name());
		int AD_Client_ID = 0;
		if (!claim.isNull()) {
			AD_Client_ID = claim.asInt();
			Env.setContext(context, Env.AD_CLIENT_ID, AD_Client_ID);
		}
		claim = jwt.getClaim(LoginClaims.AD_User_ID.name());
		if (!claim.isNull()) {
			Env.setContext(context, Env.AD_USER_ID, claim.asInt());
		}
		claim = jwt.getClaim(LoginClaims.AD_Role_ID.name());
		int AD_Role_ID = 0;
		if (!claim.isNull()) {
			AD_Role_ID = claim.asInt();
			Env.setContext(context, Env.AD_ROLE_ID, AD_Role_ID);
		}
		claim = jwt.getClaim(LoginClaims.AD_Org_ID.name());
		int AD_Org_ID = 0;
		if (!claim.isNull()) {
			AD_Org_ID = claim.asInt();
			Env.setContext(context, Env.AD_ORG_ID, AD_Org_ID);
		}
		claim = jwt.getClaim(LoginClaims.M_Warehouse_ID.name());
		if (!claim.isNull()) {
			Env.setContext(context, Env.M_WAREHOUSE_ID, claim.asInt());
		}

		if (AD_Role_ID > 0) {
			if (MRole.getDefault(context, false).isShowAcct())
				Env.setContext(context, "#ShowAcct", "Y");
			else
				Env.setContext(context, "#ShowAcct", "N");
		}

		Env.setContext(context, "#Date", new Timestamp(System.currentTimeMillis()));

		/** Define AcctSchema , Currency, HasAlias **/
		if (AD_Client_ID > 0) {
			if (MClientInfo.get(context, AD_Client_ID).getC_AcctSchema1_ID() > 0) {
				MAcctSchema primary = MAcctSchema.get(context,
						MClientInfo.get(context, AD_Client_ID).getC_AcctSchema1_ID());
				Env.setContext(context, "$C_AcctSchema_ID", primary.getC_AcctSchema_ID());
				Env.setContext(context, "$C_Currency_ID", primary.getC_Currency_ID());
				Env.setContext(context, "$HasAlias", primary.isHasAlias());
			}

			MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(context, AD_Client_ID);
			if (ass != null && ass.length > 1) {
				for (MAcctSchema as : ass) {
					if (as.getAD_OrgOnly_ID() != 0) {
						if (as.isSkipOrg(AD_Org_ID)) {
							continue;
						} else {
							Env.setContext(context, "$C_AcctSchema_ID", as.getC_AcctSchema_ID());
							Env.setContext(context, "$C_Currency_ID", as.getC_Currency_ID());
							Env.setContext(context, "$HasAlias", as.isHasAlias());
							break;
						}
					}
				}
			}
		}
	}
}
