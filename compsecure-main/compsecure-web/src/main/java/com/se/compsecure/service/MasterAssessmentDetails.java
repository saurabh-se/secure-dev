package com.se.compsecure.service;

import java.util.Calendar;

import org.springframework.stereotype.Component;

@Component
public class MasterAssessmentDetails {
	
	private String organizationName;
	private String complianceName;
	private String complianceDescription;
	private String assessmentName;
	private String assessmentDescription;
	
	private Calendar assessmentStartDate;
	private Calendar assessmentEndDate;

	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getComplianceName() {
		return complianceName;
	}
	public void setComplianceName(String complianceName) {
		this.complianceName = complianceName;
	}
	public String getComplianceDescription() {
		return complianceDescription;
	}
	public void setComplianceDescription(String complianceDescription) {
		this.complianceDescription = complianceDescription;
	}
	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	public String getAssessmentDescription() {
		return assessmentDescription;
	}
	public void setAssessmentDescription(String assessmentDescription) {
		this.assessmentDescription = assessmentDescription;
	}
	public Calendar getAssessmentStartDate() {
		return assessmentStartDate;
	}
	public void setAssessmentStartDate(Calendar assessmentStartDate) {
		this.assessmentStartDate = assessmentStartDate;
	}
	public Calendar getAssessmentEndDate() {
		return assessmentEndDate;
	}
	public void setAssessmentEndDate(Calendar assessmentEndDate) {
		this.assessmentEndDate = assessmentEndDate;
	}
	
}
