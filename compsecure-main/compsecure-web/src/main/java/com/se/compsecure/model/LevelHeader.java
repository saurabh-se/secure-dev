package com.se.compsecure.model;

import java.util.List;

public class LevelHeader {
	
	private String complianceId;

	private Integer levelHeaderId;
	private String levelHeaderName;
	private Integer levelOrder;
	
	public String getComplianceId() {
		return complianceId;
	}
	public void setComplianceId(String complianceId) {
		this.complianceId = complianceId;
	}
	public Integer getLevelHeaderId() {
		return levelHeaderId;
	}
	public void setLevelHeaderId(Integer levelHeaderId) {
		this.levelHeaderId = levelHeaderId;
	}
	public String getLevelHeaderName() {
		return levelHeaderName;
	}
	public void setLevelHeaderName(String levelHeaderName) {
		this.levelHeaderName = levelHeaderName;
	}
	public Integer getLevelOrder() {
		return levelOrder;
	}
	public void setLevelOrder(Integer levelOrder) {
		this.levelOrder = levelOrder;
	}

	
		
}
