package com.se.compsecure.model;

public class AttributeHeader {

	private String levelHeaderId;

	private Integer attributeHeaderId;
	private String attributeHeaderName;
	private Integer attributeOrder;
	
	public String getLevelHeaderId() {
		return levelHeaderId;
	}
	public void setLevelHeaderId(String levelHeaderId) {
		this.levelHeaderId = levelHeaderId;
	}
	public Integer getAttributeHeaderId() {
		return attributeHeaderId;
	}
	public void setAttributeHeaderId(Integer attributeHeaderId) {
		this.attributeHeaderId = attributeHeaderId;
	}
	public String getAttributeHeaderName() {
		return attributeHeaderName;
	}
	public void setAttributeHeaderName(String attributeHeaderName) {
		this.attributeHeaderName = attributeHeaderName;
	}
	public Integer getAttributeOrder() {
		return attributeOrder;
	}
	public void setAttributeOrder(Integer attributeOrder) {
		this.attributeOrder = attributeOrder;
	}
	
}
