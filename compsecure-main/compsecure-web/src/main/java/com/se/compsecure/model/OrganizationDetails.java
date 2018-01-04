package com.se.compsecure.model;

public class OrganizationDetails {

	private Integer organizationId;
	private String organizationName;
	private String orgAdminId;
	private String orgAdminName;
	private String orgAdminEmail;
	private String creationDate;
	private String status;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrgAdminId() {
		return orgAdminId;
	}

	public void setOrgAdminId(String orgAdminId) {
		this.orgAdminId = orgAdminId;
	}

	public String getOrgAdminEmail() {
		return orgAdminEmail;
	}

	public void setOrgAdminEmail(String orgAdminEmail) {
		this.orgAdminEmail = orgAdminEmail;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrgAdminName() {
		return orgAdminName;
	}

	public void setOrgAdminName(String orgAdminName) {
		this.orgAdminName = orgAdminName;
	}
	
}
