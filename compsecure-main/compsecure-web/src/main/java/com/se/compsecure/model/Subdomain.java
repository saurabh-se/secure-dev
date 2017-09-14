package com.se.compsecure.model;

import java.util.List;

public class Subdomain {

	private String subdomainCode;
	private String subdomainValue;
	private String domainCode;
	private String principle;
	private String objective;
	
	private List<Control> control;

	public String getSubdomainCode() {
		return subdomainCode;
	}

	public void setSubdomainCode(String subdomainCode) {
		this.subdomainCode = subdomainCode;
	}

	public String getSubdomainValue() {
		return subdomainValue;
	}

	public void setSubdomainValue(String subdomainValue) {
		this.subdomainValue = subdomainValue;
	}

	public List<Control> getControl() {
		return control;
	}

	public void setControl(List<Control> control) {
		this.control = control;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getPrinciple() {
		return principle;
	}

	public void setPrinciple(String principle) {
		this.principle = principle;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}
	
}
