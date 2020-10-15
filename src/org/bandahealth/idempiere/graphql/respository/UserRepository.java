package org.bandahealth.idempiere.graphql.respository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.base.config.Transaction;
import org.bandahealth.idempiere.base.model.MMessage_BH;
import org.bandahealth.idempiere.base.model.MUser_BH;
import org.bandahealth.idempiere.graphql.context.AuthGraphQLContext;
import org.bandahealth.idempiere.graphql.model.AuthData;
import org.bandahealth.idempiere.graphql.model.AuthResponse;
import org.bandahealth.idempiere.graphql.model.Client;
import org.bandahealth.idempiere.graphql.model.Organization;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository {

	public static String ERROR_USER_NOT_FOUND = "Could not find user";

	public List<MUser> getUsers() {
		return new Query(Env.getCtx(), MUser_BH.Table_Name, "AD_User_ID<1000001", null).list();
	}

	public AuthResponse signIn(AuthData credentials, AuthGraphQLContext context) {
		Login login = new Login(Env.getCtx());
		// retrieve list of clients the user has access to.
		KeyNamePair[] clients = login.getClients(credentials.getUsername(), credentials.getPassword());
		if (clients == null || clients.length == 0) {
			return new AuthResponse("Status.UNAUTHORIZED");
		} else {
			MUser user = MUser.get(Env.getCtx(), credentials.getUsername());
			if (user == null) {
				user = checkValidSystemUserWithNoSystemRole(clients, credentials);
			}

			if (user == null) {
				return new AuthResponse("Status.UNAUTHORIZED");
			}

			if (user.isLocked()) {
				return new AuthResponse("Status.FORBIDDEN");
			}

			if (user.isExpired()) {
				return handleUserNeedsToChangePassword(credentials);
			}

			Builder builder = JWT.create().withSubject(credentials.getUsername());
			Timestamp expiresAt = TokenUtils.getTokeExpiresAt();
			// expires after 60 minutes
			builder.withIssuer(TokenUtils.getTokenIssuer()).withExpiresAt(expiresAt);

			AuthResponse response = new AuthResponse();

			// has user changed client and role?
			if (credentials.getClientId() != null && credentials.getRoleId() != null) {
				changeLoginProperties(credentials, builder, response);
			} else {
				// set default properties
				setDefaultLoginProperties(clients, user, builder, response);
			}

			builder.withClaim(LoginClaims.AD_User_ID.name(), user.getAD_User_ID());
			Env.setContext(Env.getCtx(), Env.AD_USER_ID, user.getAD_User_ID());
			response.setUserId(user.getAD_User_ID());

			try {
				// has access to reports
//				response.setHasAccessToReports(menuDbservice.hasAccessToReports());
				// generate session token
				response.setToken(builder.sign(Algorithm.HMAC256(TokenUtils.getTokenSecret())));
				// has accepted terms of use?
//				response.setHasAcceptedTermsOfUse(TermsOfServiceDBService.hasAccepted());
				// set username
				response.setUsername(credentials.getUsername());
				// status OK.
				response.setStatus("Status.OK");
				return response;
			} catch (Exception e) {
				return new AuthResponse("Status.BAD_REQUEST");
			}
		}
	}

	/**
	 * Handle everything related to updating a user's password. Largely copied from ChangePasswordPanel > validateChangePassword
	 * @param credentials
	 * @param clients
	 */
	private void updateUsersPassword(AuthData credentials, KeyNamePair[] clients) {
		Trx trx = null;
		try {
			String trxName = Trx.createTrxName(Transaction.ChangePassword.NAME);
			trx = Trx.get(trxName, true);
			trx.setDisplayName(getClass().getName() + Transaction.ChangePassword.SUFFIX_DISPLAY);

			for (KeyNamePair client : clients) {
				int clientId = client.getKey();
				Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, clientId);
				MUser clientUser = MUser.get(Env.getCtx(), credentials.getUsername());
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
	private AuthResponse handleUserNeedsToChangePassword(AuthData credentials) {
		List<String> securityQuestions = new ArrayList<>();

		for (int i = 1; i <= MMessage_BH.NO_OF_SECURITY_QUESTION; i++) {
			securityQuestions.add(Msg.getMsg(Env.getCtx(), MMessage_BH.SECURITY_QUESTION_PREFIX + i));
		}
		AuthResponse response = new AuthResponse();
		response.setUsername(credentials.getUsername());
		response.setNeedsToResetPassword(true);
		response.setSecurityQuestions(securityQuestions);
		response.setStatus("Status.OK");
		return response;
	}

	/**
	 * This function will be called when a user has changed login credentials i.e
	 * client, role, warehouse, organization
	 *
	 * @param credentials
	 * @param builder
	 */
	private void changeLoginProperties(AuthData credentials, Builder builder, AuthResponse response) {
		// set client id
		if (credentials.getClientId() != null) {
			MClient client = MClient.get(Env.getCtx(), credentials.getClientId());
			if (client != null) {
				response.getClients().add(new Client(Env.getCtx(), credentials.getClientId(), null));

				Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, credentials.getClientId());
				builder.withClaim(LoginClaims.AD_Client_ID.name(), credentials.getClientId());
			}
		}

		// set role
		if (credentials.getRoleId() != null) {
			Env.setContext(Env.getCtx(), Env.AD_ROLE_ID, credentials.getRoleId());
			builder.withClaim(LoginClaims.AD_Role_ID.name(), credentials.getRoleId());
			response.setRoleId(credentials.getRoleId());
		}

		// check organization
		if (credentials.getOrganizationId() != null) {
			Env.setContext(Env.getCtx(), Env.AD_ORG_ID, credentials.getOrganizationId());
			builder.withClaim(LoginClaims.AD_Org_ID.name(), credentials.getOrganizationId());
		}

		// check warehouse
		if (credentials.getWarehouseId() != null) {
			Env.setContext(Env.getCtx(), Env.M_WAREHOUSE_ID, credentials.getWarehouseId());
			builder.withClaim(LoginClaims.M_Warehouse_ID.name(), credentials.getWarehouseId());
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
	private void setDefaultLoginProperties(KeyNamePair[] clients, MUser user, Builder builder, AuthResponse response) {
		// parse all clients that the user has access to.
		for (KeyNamePair client : clients) {
			Client mClient = new Client(Env.getCtx(), client.getKey(), null);

			// set default client
			if (clients.length == 1) {
				Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, mClient.getAD_Client_ID());
				builder.withClaim(LoginClaims.AD_Client_ID.name(), mClient.getAD_Client_ID());
			}

			// check orgs.
			MOrg[] organizations = MOrg.getOfClient(new MClient(Env.getCtx(), mClient.getAD_Client_ID(), null));
			for (MOrg org : organizations) {
				Organization organization = new Organization(Env.getCtx(), org.getAD_Org_ID(), null);
				// set default org
				if (organizations.length == 1) {
					Env.setContext(Env.getCtx(), Env.AD_ORG_ID, org.getAD_Org_ID());
					builder.withClaim(LoginClaims.AD_Org_ID.name(), org.getAD_Org_ID());
				}

				// check roles
				MRole[] roles = user.getRoles(org.getAD_Org_ID());
				for (MRole role : roles) {
					organization.getRoles().add(role);

					if (roles.length == 1) {
						Env.setContext(Env.getCtx(), Env.AD_ROLE_ID, role.getAD_Role_ID());
						builder.withClaim(LoginClaims.AD_Role_ID.name(), role.getAD_Role_ID());
						response.setRoleId(role.getAD_Role_ID());
					}
				}

				organization.getWarehouses().addAll(Arrays.asList(MWarehouse.getForOrg(Env.getCtx(), org.getAD_Org_ID())));

				// set default warehouse
				if (organization.getWarehouses().size() == 1) {
					MWarehouse warehouse = organization.getWarehouses().get(0);
					Env.setContext(Env.getCtx(), Env.M_WAREHOUSE_ID, warehouse.get_ID());
					builder.withClaim(LoginClaims.M_Warehouse_ID.name(), warehouse.get_ID());
				}

				mClient.getOrganizations().add(organization);
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
	private MUser checkValidSystemUserWithNoSystemRole(KeyNamePair[] clients, AuthData credentials) {
		MUser user = null;
		for (KeyNamePair client : clients) {
			// update context with client id
			Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, client.getKey());

			user = MUser.get(Env.getCtx(), credentials.getUsername());
			if (user != null) {
				break;
			}
		}

		return user;
	}
}
