package com.se.compsecure.utility;

public class ValidityObj {

	private String userId;
	private boolean validity;
	
	private String userName;
	private String changedPassword;
	private String securityQuestion;
	private String securityAnswer;
	
	public ValidityObj(boolean validity,String userId) {
		this.userId = userId;
		this.validity = validity;
	}
	
	
	
//	public ValidityObj(boolean validity, String userName, String changedPassword, String securityQuestion,
//			String securityAnswer) {
//		this.validity = validity;
//		this.userName = userName;
//		this.changedPassword = changedPassword;
//		this.securityQuestion = securityQuestion;
//		this.securityAnswer = securityAnswer;
//	}
//
//
//
//	public ValidityObj(String userId, boolean validity, String userName, String changedPassword,
//			String securityQuestion, String securityAnswer) {
//		this.userId = userId;
//		this.validity = validity;
//		this.userName = userName;
//		this.changedPassword = changedPassword;
//		this.securityQuestion = securityQuestion;
//		this.securityAnswer = securityAnswer;
//	}

	public ValidityObj() {
	}



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getValidity() {
		return validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChangedPassword() {
		return changedPassword;
	}

	public void setChangedPassword(String changedPassword) {
		this.changedPassword = changedPassword;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
}
