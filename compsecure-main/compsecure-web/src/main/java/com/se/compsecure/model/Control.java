package com.se.compsecure.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Control {
	
	private Integer controlId;
	private Integer levelHeaderId;	
	
	private String controlCode;
	private String controlValue;
	private String controlHeaderName;
	
	private String subdomainId;
	
	@Autowired
	Questions questions;

	public Integer getControlId() {
		return controlId;
	}
	public void setControlId(Integer controlId) {
		this.controlId = controlId;
	}
	public Integer getLevelHeaderId() {
		return levelHeaderId;
	}
	public void setLevelHeaderId(Integer levelHeaderId) {
		this.levelHeaderId = levelHeaderId;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public String getControlValue() {
		return controlValue;
	}
	public void setControlValue(String controlValue) {
		this.controlValue = controlValue;
	}
	public String getControlHeaderName() {
		return controlHeaderName;
	}
	public void setControlHeaderName(String controlHeaderName) {
		this.controlHeaderName = controlHeaderName;
	}
	public Questions getQuestions() {
		return questions;
	}
	public void setQuestions(Questions questions) {
		this.questions = questions;
	}
	public String getSubdomainId() {
		return subdomainId;
	}
	public void setSubdomainId(String subdomainId) {
		this.subdomainId = subdomainId;
	}
	
	
}
