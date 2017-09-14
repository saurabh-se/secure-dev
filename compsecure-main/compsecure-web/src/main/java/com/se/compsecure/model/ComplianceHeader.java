package com.se.compsecure.model;

import java.util.List;

public class ComplianceHeader {
	
	private Integer complianceId;
	
	private String complianceCode;
	private String complianceName;
	private String complianceDescription;
	private List<LevelHeader> subdomain;

	private String regulatorId;
	private Integer noOfLevels;
	
	
	public Integer getComplianceId() {
		return complianceId;
	}
	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}
	public String getComplianceCode() {
		return complianceCode;
	}
	public void setComplianceCode(String complianceCode) {
		this.complianceCode = complianceCode;
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
	public List<LevelHeader> getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(List<LevelHeader> subdomain) {
		this.subdomain = subdomain;
	}
	public String getRegulatorId() {
		return regulatorId;
	}
	public void setRegulatorId(String regulatorId) {
		this.regulatorId = regulatorId;
	}
	public Integer getNoOfLevels() {
		return noOfLevels;
	}
	public void setNoOfLevels(Integer noOfLevels) {
		this.noOfLevels = noOfLevels;
	}
	
}
