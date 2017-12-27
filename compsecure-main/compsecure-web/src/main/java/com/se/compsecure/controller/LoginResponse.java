package com.se.compsecure.controller;

public class LoginResponse {
	
	private String token;
	private String roleId;
	private String userId;
	
	
	
	public LoginResponse(String token, String roleId,String userId) {
		this.token = token;
		this.roleId = roleId;
		this.userId = userId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
