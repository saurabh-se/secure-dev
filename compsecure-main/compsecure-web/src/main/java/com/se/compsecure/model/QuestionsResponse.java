package com.se.compsecure.model;

public class QuestionsResponse {
	
	private String controlCode;
	private String questionCode;
	private String questionResponse;
	private String questionRemarks;
	private String assessmentId;
	
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public String getQuestionResponse() {
		return questionResponse;
	}
	public void setQuestionResponse(String questionResponse) {
		this.questionResponse = questionResponse;
	}
	public String getQuestionRemarks() {
		return questionRemarks;
	}
	public void setQuestionRemarks(String questionRemarks) {
		this.questionRemarks = questionRemarks;
	}
	public String getQuestionCode() {
		return questionCode;
	}
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	public String getAssessmentId() {
		return assessmentId;
	}
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}
	
}
