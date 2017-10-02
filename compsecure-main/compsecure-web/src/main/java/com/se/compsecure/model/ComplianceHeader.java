package com.se.compsecure.model;

import java.util.List;

public class ComplianceHeader {
	
	private Integer complianceId;
	
	private String complianceCode;
	private String complianceName;
	private String complianceDescription;
	
	private List<Domain> domains;
//	private List<Subdomain> subdomain;
	

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
//	public List<Subdomain> getSubdomain() {
//		return subdomain;
//	}
//	public void setSubdomain(List<Subdomain> subdomain) {
//		this.subdomain = subdomain;
//	}
	public List<Domain> getDomains() {
		return domains;
	}
	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}
	
}
