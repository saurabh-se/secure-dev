package com.se.compsecure.model;

import java.util.Calendar;

public class AssessmentDetails {

	private String assessmentId;
	private String assessmentName;
	private String organizationId;
	private String assessmentStatus;
	private String remarks;
		
	private Calendar assessmentStartDate;
	private Calendar assessmentToDate;
	
	
	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Calendar getAssessmentStartDate() {
		return assessmentStartDate;
	}

	public void setAssessmentStartDate(Calendar assessmentStartDate) {
		this.assessmentStartDate = assessmentStartDate;
	}

	public Calendar getAssessmentToDate() {
		return assessmentToDate;
	}

	public void setAssessmentToDate(Calendar assessmentToDate) {
		this.assessmentToDate = assessmentToDate;
	}
	
}
