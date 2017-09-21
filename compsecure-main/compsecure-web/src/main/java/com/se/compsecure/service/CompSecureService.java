package com.se.compsecure.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.se.compsecure.model.AssessmentDetails;
import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.ControlEffectiveness;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Maturity;
import com.se.compsecure.model.OrganizationDetails;
import com.se.compsecure.model.Questionnaire;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.QuestionsResponse;
import com.se.compsecure.model.Subdomain;
import com.se.compsecure.model.UploadFile;
import com.se.compsecure.model.User;
import com.se.compsecure.model.UserRoles;

@Component
public interface CompSecureService {
	
	List<OrganizationDetails> getOrganizationList();
	
	String getOrganizationBasedOnLogin(String userId);
	
	MasterAssessmentDetails getMasterAssessmentDetails(String organizationId);
	
	// 1. Get the assessment details 
	List<AssessmentDetails> getAssessmentDetails(String organization);

	// 2. Get the questionnaire for that assessment
	List<Questionnaire> getQuestionnaire(Integer levelHeaderId);
	
	// 3. Get the Control Effectiveness details
	List<Control> getControlDetails(Integer levelHeaderId);
	
	// 4. Get the Maturity Effectiveness details
	List<Maturity> getMaturityDetails(Integer levelHeaderId);

	List<ComplianceHeader> getComplianceDetails(String assessmentId);
	
	List<Entry<String , Domain>> getDomainDetails(String assessmentId);

	List<Questions> getQuestions(String controlCode);

	List<Questions> getComplianceQuestions(String complianceName, String assessmentId);

	Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes);

	User authenticate(User user);

	UserRoles getRole(User user);

	List<OrganizationDetails> getOrganizationList(String userId);

	void save(MultipartFile fileUpload, String docUploadType,Integer assessmentId,String controlCode) throws IOException;

	void saveControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2);

	List<Questions> getQuestions(String controlCode, String assessmentId);

	List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId);

	List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceId);
}
