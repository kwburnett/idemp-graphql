package org.bandahealth.idempiere.graphql.model;

import org.bandahealth.idempiere.base.model.MUser_BH;
import org.compiere.model.MClient;

import java.util.ArrayList;
import java.util.List;

/**
 * The authentication response returned from the methods that are for signing in.
 */
public class AuthenticationResponse {
	private String token;
	private MUser_BH user;
	private String username;
	private List<MClient> clients = new ArrayList<>();
	private String roleId;
	private boolean needsToResetPassword;
	private List<String> securityQuestions;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MUser_BH getUser() {
		return user;
	}

	public void setUser(MUser_BH user) {
		this.user = user;
	}

	public List<MClient> getClients() {
		return clients;
	}

	public void setClients(List<MClient> clients) {
		this.clients = clients;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean getNeedsToResetPassword() {
		return needsToResetPassword;
	}

	public void setNeedsToResetPassword(boolean needsToResetPassword) {
		this.needsToResetPassword = needsToResetPassword;
	}

	public List<String> getSecurityQuestions() {
		return securityQuestions;
	}

	public void setSecurityQuestions(List<String> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
}
