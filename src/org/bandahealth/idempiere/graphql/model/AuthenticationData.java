package org.bandahealth.idempiere.graphql.model;

public class AuthenticationData {
	private String username;
	private String password;
	private String newPassword;
	private String securityQuestion;
	private String answer;
	private Integer clientId;
	private Integer roleId;
	private Integer organizationId;
	private Integer warehouseId;

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

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer adClientId) {
		this.clientId = adClientId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer adRoleId) {
		this.roleId = adRoleId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer adOrganizationId) {
		this.organizationId = adOrganizationId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer adWarehouseId) {
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
