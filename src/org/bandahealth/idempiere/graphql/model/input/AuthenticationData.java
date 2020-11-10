package org.bandahealth.idempiere.graphql.model.input;

public class AuthenticationData {
	private String username;
	private String password;
	private String newPassword;
	private String securityQuestion;
	private String answer;
	private String clientId;
	private String roleId;
	private String organizationId;
	private String warehouseId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String adClientId) {
		this.clientId = adClientId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String adRoleId) {
		this.roleId = adRoleId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String adOrganizationId) {
		this.organizationId = adOrganizationId;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String adWarehouseId) {
		this.warehouseId = adWarehouseId;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
