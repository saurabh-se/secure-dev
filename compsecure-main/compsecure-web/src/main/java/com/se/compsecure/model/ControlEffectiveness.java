package com.se.compsecure.model;

import java.util.List;

public class ControlEffectiveness {
	
	private String controlCode;
	private String docEffectiveness;
	private String docEffRemarks;
	private String implEffectiveness;
	private String implEffRemarks;
	private String recEffectiveness;
	private String recEffRemarks;
	private String docEffEvidenceId;
	private String implEffEvidenceId;
	private String recEffEvidenceId;
	
	private List<String> docEffEvidences;
	private List<String> implEffEvidences;
	private List<String> recEffEvidences;
	
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public String getDocEffectiveness() {
		return docEffectiveness;
	}
	public void setDocEffectiveness(String docEffectiveness) {
		this.docEffectiveness = docEffectiveness;
	}
	public String getDocEffRemarks() {
		return docEffRemarks;
	}
	public void setDocEffRemarks(String docEffRemarks) {
		this.docEffRemarks = docEffRemarks;
	}
	public String getImplEffectiveness() {
		return implEffectiveness;
	}
	public void setImplEffectiveness(String implEffectiveness) {
		this.implEffectiveness = implEffectiveness;
	}
	public String getImplEffRemarks() {
		return implEffRemarks;
	}
	public void setImplEffRemarks(String implEffRemarks) {
		this.implEffRemarks = implEffRemarks;
	}
	public String getRecEffectiveness() {
		return recEffectiveness;
	}
	public void setRecEffectiveness(String recEffectiveness) {
		this.recEffectiveness = recEffectiveness;
	}
	public String getRecEffRemarks() {
		return recEffRemarks;
	}
	public void setRecEffRemarks(String recEffRemarks) {
		this.recEffRemarks = recEffRemarks;
	}
	public String getDocEffEvidenceId() {
		return docEffEvidenceId;
	}
	public void setDocEffEvidenceId(String docEffEvidenceId) {
		this.docEffEvidenceId = docEffEvidenceId;
	}
	public String getImplEffEvidenceId() {
		return implEffEvidenceId;
	}
	public void setImplEffEvidenceId(String implEffEvidenceId) {
		this.implEffEvidenceId = implEffEvidenceId;
	}
	public String getRecEffEvidenceId() {
		return recEffEvidenceId;
	}
	public void setRecEffEvidenceId(String recEffEvidenceId) {
		this.recEffEvidenceId = recEffEvidenceId;
	}
	public List<String> getDocEffEvidences() {
		return docEffEvidences;
	}
	public void setDocEffEvidences(List<String> docEffEvidences) {
		this.docEffEvidences = docEffEvidences;
	}
	public List<String> getImplEffEvidences() {
		return implEffEvidences;
	}
	public void setImplEffEvidences(List<String> implEffEvidences) {
		this.implEffEvidences = implEffEvidences;
	}
	public List<String> getRecEffEvidences() {
		return recEffEvidences;
	}
	public void setRecEffEvidences(List<String> recEffEvidences) {
		this.recEffEvidences = recEffEvidences;
	}
}
