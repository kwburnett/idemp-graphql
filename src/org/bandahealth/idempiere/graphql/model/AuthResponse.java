package org.bandahealth.idempiere.graphql.model;

import org.compiere.model.MClient;

import java.util.ArrayList;
import java.util.List;

public class AuthResponse {
	private String token;
	private String status;
	private int userId;
	private String username;
	private List<Client> clients = new ArrayList<>();
	private boolean hasAcceptedTermsOfUse;
	private int roleId;
	private boolean needsToResetPassword;
	private List<String> securityQuestions;
	private boolean hasAccessToReports;

	public AuthResponse() {
	}

	public AuthResponse(String status) {
		this.status = status;
	}

	public AuthResponse(String token, String status, int userId) {
		this.token = token;
		this.status = status;
		this.userId = userId;
	}

	public AuthResponse(String token, String status) {
		this.token = token;
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public boolean isHasAcceptedTermsOfUse() {
		return hasAcceptedTermsOfUse;
	}

	public void setHasAcceptedTermsOfUse(boolean hasAcceptedTermsOfUse) {
		this.hasAcceptedTermsOfUse = hasAcceptedTermsOfUse;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
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

	public boolean isHasAccessToReports() {
		return hasAccessToReports;
	}

	public void setHasAccessToReports(boolean hasAccessToReports) {
		this.hasAccessToReports = hasAccessToReports;
	}
}
