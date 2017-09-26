package com.se.compsecure.dao;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.multipart.MultipartFile;

import com.se.compsecure.model.AssessmentDetails;
import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.ControlEffectiveness;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.OrganizationDetails;
import com.se.compsecure.model.Questionnaire;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.QuestionsResponse;
import com.se.compsecure.model.Regulator;
import com.se.compsecure.model.UploadFile;
import com.se.compsecure.model.User;
import com.se.compsecure.model.UserRoles;

public interface CompSecureDAO {
	
	void addFramework(Regulator framework);
	
	List<AssessmentDetails> getAssessmentDetails(String organization);
	
	Control getControls(String controlCode);
	
	List<Questions> getQuestions(String controlId);
	
	List<OrganizationDetails> getOrganizationList();
	
	String getOrganizationBasedOnLogin(String userId);
	
	List<ComplianceHeader> getComplianceDetails(String assessmentId);
	
	List<Entry<String , Domain>> getDomainDetails(String assessmentId, String complianceId);

	List<Questions> getComplianceQuestions(String complianceName, String assessmentId);

	Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes);

	User authenticateUser(User user);

	UserRoles getRole(User user);

	List<OrganizationDetails> getOrganizationList(String userId);

	void uploadFile(UploadFile uploadFile, String docToUpload);

	void saveControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2);

	List<Questions> getQuestions(String controlCode, String assessmentId);

	List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId);

	List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceId);

	Integer alterComplianceQuestionsResponse(List<QuestionsResponse> questionResponseList);

	void createCompliance(ComplianceHeader complianceHeader);

	void saveAssessmentDetails(AssessmentDetails assessmentDetails);

	List<Questions> getComplianceQuestionsForExistingAssessment(String assessmentId);

}
