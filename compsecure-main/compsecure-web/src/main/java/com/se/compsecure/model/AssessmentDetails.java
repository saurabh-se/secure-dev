package com.se.compsecure.model;

public class AssessmentDetails {

	private String assessmentId;
	private String assessmentName;
	private String organizationId;
	private String assessmentStatus;
	private String assessmentDesc;
		
	private String assessmentStartDate;
	private String assessmentToDate;

	private String complianceId;
	private String complianceDesc;
	
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

	public String getAssessmentStartDate() {
		return assessmentStartDate;
	}

	public void setAssessmentStartDate(String assessmentStartDate) {
		this.assessmentStartDate = assessmentStartDate;
	}

	public String getAssessmentToDate() {
		return assessmentToDate;
	}

	public void setAssessmentToDate(String assessmentToDate) {
		this.assessmentToDate = assessmentToDate;
	}

	public String getAssessmentDesc() {
		return assessmentDesc;
	}

	public void setAssessmentDesc(String assessmentDesc) {
		this.assessmentDesc = assessmentDesc;
	}

	public String getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(String complianceId) {
		this.complianceId = complianceId;
	}

	public String getComplianceDesc() {
		return complianceDesc;
	}

	public void setComplianceDesc(String complianceDesc) {
		this.complianceDesc = complianceDesc;
	}
}
