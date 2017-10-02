package com.se.compsecure.model;

public class Maturity {

	private Integer maturityId;
	private Integer levelHeaderId;
	
	private String maturityDefinition;
	private String maturityCriteria;
	private String maturityExplanation;
	
	public Integer getLevelHeaderId() {
		return levelHeaderId;
	}
	public void setLevelHeaderId(Integer levelHeaderId) {
		this.levelHeaderId = levelHeaderId;
	}
	public Integer getMaturityId() {
		return maturityId;
	}
	public void setMaturityId(Integer maturityId) {
		this.maturityId = maturityId;
	}
	public String getMaturityDefinition() {
		return maturityDefinition;
	}
	public void setMaturityDefinition(String maturityDefinition) {
		this.maturityDefinition = maturityDefinition;
	}
	public String getMaturityCriteria() {
		return maturityCriteria;
	}
	public void setMaturityCriteria(String maturityCriteria) {
		this.maturityCriteria = maturityCriteria;
	}
	public String getMaturityExplanation() {
		return maturityExplanation;
	}
	public void setMaturityExplanation(String maturityExplanation) {
		this.maturityExplanation = maturityExplanation;
	}
	
	
}
