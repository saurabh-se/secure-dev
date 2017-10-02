package com.se.compsecure.model;

public class Regulator {
	
	private Integer regulatorId;
	private String regulatorName;
	
	public Regulator(String regulatorName) {
		this.regulatorName = regulatorName;
	}
	
	public Integer getRegulatorId() {
		return regulatorId;
	}
	public void setRegulatorId(Integer regulatorId) {
		this.regulatorId = regulatorId;
	}
	public String getRegulatorName() {
		return regulatorName;
	}
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}
	

	
	
}
