package org.bandahealth.idempiere.graphql.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.config.Transaction;
import org.bandahealth.idempiere.base.model.MMessage_BH;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.model.input.AuthenticationData;
import org.bandahealth.idempiere.graphql.model.AuthenticationResponse;
import org.bandahealth.idempiere.graphql.utils.LoginClaims;
import org.bandahealth.idempiere.graphql.utils.TokenUtils;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Login;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AuthenticationRepository {

	public static String ERROR_USER_NOT_FOUND = "Could not find user";

	/**
	 * Authentication Service Accepts Username, password and generates a session
	 * token. It attempts to set default client, role, warehouse, org values for
	 * clients, else return a list where multiple values are found
	 *
	 * @param credentials
	 * @param idempiereContext The context since Env.getCtx() isn't thread-safe
	 * @return
	 */
	public AuthenticationResponse signIn(AuthenticationData credentials, Properties idempiereContext) {
		Login login = new Login(idempiereContext);
		// retrieve list of clients the user has access to.
		KeyNamePair[] clients = login.getClients(credentials.getUsername(), credentials.getPassword());
		if (clients == null || clients.length == 0) {
			throw new AdempiereException("Incorrect username or password");
		} else {
			MUser user = MUser.get(idempiereContext, credentials.getUsername());
			if (user == null) {
				user = checkValidSystemUserWithNoSystemRole(clients, credentials, idempiereContext);
			}

			if (user == null) {
				throw new AdempiereException("Incorrect username or password");
			}

			if (user.isLocked()) {
				throw new AdempiereException("Forbidden - user is locked");
			}

			if (user.isExpired()) {
				return handleUserNeedsToChangePassword(credentials, idempiereContext);
			}

			JWTCreator.Builder builder = JWT.create().withSubject(credentials.getUsername());
			Timestamp expiresAt = TokenUtils.getTokeExpiresAt();
			// expires after 60 minutes
			builder.withIssuer(TokenUtils.getTokenIssuer()).withExpiresAt(expiresAt);

			AuthenticationResponse response = new AuthenticationResponse();

			// has user changed client and role?
			if (credentials.getClientId() != null && credentials.getRoleId() != null) {
				changeLoginProperties(credentials, builder, response, idempiereContext);
			} else {
				// set default properties
				setDefaultLoginProperties(clients, user, builder, response, idempiereContext);
			}

			builder.withClaim(LoginClaims.AD_User_ID.name(), user.getAD_User_ID());
			Env.setContext(idempiereContext, Env.AD_USER_ID, user.getAD_User_ID());
			response.setUser(new MUser_BH(idempiereContext, user.getAD_User_ID(), null));

			try {
				// generate session token
				response.setToken(builder.sign(Algorithm.HMAC256(TokenUtils.getTokenSecret())));
				// set username
				response.setUsername(credentials.getUsername());
				return response;
			} catch (Exception e) {
				throw new AdempiereException("Bad request");
			}
		}
	}

	public AuthenticationResponse changePassword(AuthenticationData credentials, Properties idempiereContext) {
		Login login = new Login(idempiereContext);
		// retrieve list of clients the user has access to.
		KeyNamePair[] clients = login.getClients(credentials.getUsername(), credentials.getPassword());
		if (clients == null || clients.length == 0) {
			throw new AdempiereException("Incorrect username or password");
		}

		MUser user = MUser.get(idempiereContext, credentials.getUsername());
		if (user == null) {
			user = checkValidSystemUserWithNoSystemRole(clients, credentials, idempiereContext);
		}

		/**
		 * Copied from ChangePasswordPanel > validateChangePassword
		 */
		if (org.compiere.util.Util.isEmpty(credentials.getPassword())) {
			throw new IllegalArgumentException(org.compiere.util.Msg.getMsg(idempiereContext, MMessage_BH.OLD_PASSWORD_MANDATORY));
		}

		if (Util.isEmpty(credentials.getNewPassword())) {
			throw new IllegalArgumentException(org.compiere.util.Msg.getMsg(idempiereContext, MMessage_BH.NEW_PASSWORD_MANDATORY));
		}

		// TODO: Add this back in if we start using these
//		if (Util.isEmpty(credentials.getSecurityQuestion())) {
//			throw new IllegalArgumentException(Msg.getMsg(context, MADMessage_BH.SECURITY_QUESTION_MANDATORY));
//		}
//
//		if (Util.isEmpty(credentials.getAnswer())) {
//			throw new IllegalArgumentException(Msg.getMsg(context, MADMessage_BH.ANSWER_MANDATORY));
//		}

		if (org.compiere.model.MSysConfig.getBooleanValue(org.compiere.model.MSysConfig.CHANGE_PASSWORD_MUST_DIFFER, true)) {
			if (credentials.getPassword().equals(credentials.getNewPassword())) {
				throw new IllegalArgumentException(Msg.getMsg(idempiereContext, MMessage_BH.NEW_PASSWORD_MUST_DIFFER));
			}
		}

		updateUsersPassword(credentials, clients, idempiereContext);
		return this.signIn(credentials, idempiereContext);
	}

	/**
	 * Handle everything related to updating a user's password. Largely copied from ChangePasswordPanel > validateChangePassword
	 *
	 * @param credentials
	 * @param clients
	 */
	private void updateUsersPassword(AuthenticationData credentials, KeyNamePair[] clients, Properties idempiereContext) {
		Trx trx = null;
		try {
			String trxName = Trx.createTrxName(Transaction.ChangePassword.NAME);
			trx = Trx.get(trxName, true);
			trx.setDisplayName(getClass().getName() + Transaction.ChangePassword.SUFFIX_DISPLAY);

			for (KeyNamePair client : clients) {
				int clientId = client.getKey();
				Env.setContext(idempiereContext, Env.AD_CLIENT_ID, clientId);
				MUser clientUser = MUser.get(idempiereContext, credentials.getUsername());
				if (clientUser == null) {
					trx.rollback();
					throw new AdempiereException(ERROR_USER_NOT_FOUND);
				}

				clientUser.set_ValueOfColumn(MUser.COLUMNNAME_Password, credentials.getNewPassword()); // will be hashed and validate on saveEx
				clientUser.setIsExpired(false);
				// TODO: Add this back in if we start using these
//				clientUser.setSecurityQuestion(credentials.getSecurityQuestion());
//				clientUser.setAnswer(credentials.getAnswer());
				clientUser.saveEx(trx.getTrxName());
			}

			trx.commit();
		} catch (AdempiereException e) {
			if (trx != null)
				trx.rollback();
			throw e;
		} finally {
			if (trx != null)
				trx.close();
		}
		// The user's password has been updated, so update the credentials object, too
		credentials.setPassword(credentials.getNewPassword());
	}

	/**
	 * The user needs to change their credentials, so set the appropriate data
	 *
	 * @param credentials
	 * @return
	 */
	private AuthenticationResponse handleUserNeedsToChangePassword(AuthenticationData credentials,
			Properties idempiereContext) {
		List<String> securityQuestions = new ArrayList<>();

		for (int i = 1; i <= MMessage_BH.NO_OF_SECURITY_QUESTION; i++) {
			securityQuestions.add(Msg.getMsg(idempiereContext, MMessage_BH.SECURITY_QUESTION_PREFIX + i));
		}
		AuthenticationResponse response = new AuthenticationResponse();
		response.setUsername(credentials.getUsername());
		response.setNeedsToResetPassword(true);
		response.setSecurityQuestions(securityQuestions);
		return response;
	}

	/**
	 * This function will be called when a user has changed login credentials i.e
	 * client, role, warehouse, organization
	 *
	 * @param credentials
	 * @param builder
	 */
	private void changeLoginProperties(AuthenticationData credentials, JWTCreator.Builder builder,
			AuthenticationResponse response, Properties idempiereContext) {
		// set client id
		if (credentials.getClientId() != null) {
			MClient client = new Query(idempiereContext, MClient.Table_Name, MClient.COLUMNNAME_AD_Client_UU +
					"=?", null).setParameters(credentials.getClientId()).first();
			if (client != null) {
				response.getClients().add(client);

				Env.setContext(idempiereContext, Env.AD_CLIENT_ID, client.getAD_Client_ID());
				builder.withClaim(LoginClaims.AD_Client_ID.name(), client.getAD_Client_ID());
			}
		}

		// set role
		if (credentials.getRoleId() != null) {
			MRole role = new Query(idempiereContext, MRole.Table_Name, MRole.COLUMNNAME_AD_Role_UU + "=?",
					null).setParameters(credentials.getRoleId()).first();
			Env.setContext(idempiereContext, Env.AD_ROLE_ID, role.getAD_Role_ID());
			builder.withClaim(LoginClaims.AD_Role_ID.name(), role.getAD_Role_ID());
			response.setRoleId(credentials.getRoleId());
		}

		// check organization
		if (credentials.getOrganizationId() != null) {
			MOrg organization = new Query(idempiereContext, MOrg.Table_Name, MOrg.COLUMNNAME_AD_Org_UU + "=?",
					null).setParameters(credentials.getOrganizationId()).first();
			Env.setContext(idempiereContext, Env.AD_ORG_ID, organization.getAD_Org_ID());
			builder.withClaim(LoginClaims.AD_Org_ID.name(), organization.getAD_Org_ID());
		}

		// check warehouse
		if (credentials.getWarehouseId() != null) {
			MWarehouse warehouse = new Query(idempiereContext, MWarehouse.Table_Name,
					MWarehouse.COLUMNNAME_M_Warehouse_UU + "=?", null)
					.setParameters(credentials.getWarehouseId()).first();
			Env.setContext(idempiereContext, Env.M_WAREHOUSE_ID, warehouse.get_ID());
			builder.withClaim(LoginClaims.M_Warehouse_ID.name(), warehouse.get_ID());
		}

	}

	/**
	 * Set default properties
	 *
	 * @param clients
	 * @param user
	 * @param builder
	 * @param response
	 */
	private void setDefaultLoginProperties(KeyNamePair[] clients, MUser user, JWTCreator.Builder builder,
			AuthenticationResponse response, Properties idempiereContext) {
		// parse all clients that the user has access to.
		for (KeyNamePair client : clients) {
			MClient mClient = new MClient(idempiereContext, client.getKey(), null);

			// set default client
			if (clients.length == 1) {
				Env.setContext(idempiereContext, Env.AD_CLIENT_ID, mClient.getAD_Client_ID());
				builder.withClaim(LoginClaims.AD_Client_ID.name(), mClient.getAD_Client_ID());
			}

			// check orgs.
			MOrg[] organizations = MOrg.getOfClient(new org.compiere.model.MClient(idempiereContext,
					mClient.getAD_Client_ID(), null));
			for (MOrg org : organizations) {
				// set default org
				if (organizations.length == 1) {
					Env.setContext(idempiereContext, Env.AD_ORG_ID, org.getAD_Org_ID());
					builder.withClaim(LoginClaims.AD_Org_ID.name(), org.getAD_Org_ID());
				}

				// check roles
				MRole[] roles = user.getRoles(org.getAD_Org_ID());
				if (roles.length == 1) {
					MRole role = roles[0];
					Env.setContext(idempiereContext, Env.AD_ROLE_ID, role.getAD_Role_ID());
					builder.withClaim(LoginClaims.AD_Role_ID.name(), role.getAD_Role_ID());
					response.setRoleId(role.getAD_Role_UU());
				}

				MWarehouse[] warehouses = MWarehouse.getForOrg(idempiereContext, org.getAD_Org_ID());

				// set default warehouse
				if (warehouses.length == 1) {
					MWarehouse warehouse = warehouses[0];
					Env.setContext(idempiereContext, Env.M_WAREHOUSE_ID, warehouse.get_ID());
					builder.withClaim(LoginClaims.M_Warehouse_ID.name(), warehouse.get_ID());
				}
			}

			response.getClients().add(mClient);
		}
	}

	/**
	 * Check valid system users with no system role.
	 *
	 * @param clients
	 * @param credentials
	 * @return
	 */
	private MUser checkValidSystemUserWithNoSystemRole(KeyNamePair[] clients, AuthenticationData credentials,
			Properties idempiereContext) {
		MUser user = null;
		for (KeyNamePair client : clients) {
			// update context with client id
			Env.setContext(idempiereContext, Env.AD_CLIENT_ID, client.getKey());

			user = MUser.get(idempiereContext, credentials.getUsername());
			if (user != null) {
				break;
			}
		}

		return user;
	}
}
