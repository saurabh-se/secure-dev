package com.se.compsecure.model;

import java.util.List;

public class Domain {
	
	private String domainCode;
	private String domainName;
	
	private List<Subdomain> subdomain;
	
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public List<Subdomain> getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(List<Subdomain> subdomain) {
		this.subdomain = subdomain;
	}

}
