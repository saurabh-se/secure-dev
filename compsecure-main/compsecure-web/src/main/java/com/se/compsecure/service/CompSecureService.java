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
import com.se.compsecure.utility.ValidityObj;

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
	String[] getMaturityDetails(String complianceId);

	List<ComplianceHeader> getComplianceDetails(String assessmentId);
	
	List<Entry<String , Domain>> getDomainDetails(String assessmentId, String complianceId);

	List<Questions> getQuestions(String controlCode);

	List<Questions> getComplianceQuestions(String complianceName, String assessmentId);

	Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes,String assessmentId);

	User authenticate(User user, String salt);

	UserRoles getRole(User user);

	List<OrganizationDetails> getOrganizationList(String userId);

	void save(MultipartFile fileUpload, String docUploadType,String assessmentId,String controlCode) throws IOException;

	void saveControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2, String assessmentId);

	List<Questions> getQuestions(String controlCode, String assessmentId);

	List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId);

	List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceId);

	Integer alterComplianceQuestionsResponse(List<QuestionsResponse> questionResponseList);

	void createCompliance(ComplianceHeader complianceHeader);

	String saveAssessmentDetails(AssessmentDetails assessmentDetails, String self_assessment_option);

	List<Questions> getComplianceQuestionsForExistingAssessment(String assessmentId);

	List<Entry<String, Domain>> getCompleteDetails(String assessmentId, String complianceId);

	List<ControlEffectiveness> getControlEffectivenessDetails(String assessmentId, String complianceId);

	String getComplianceId(String complianceDescription);

	void saveComplianceDefinitionData(ComplianceHeader complianceHeader);

	Map<String, String> getCompliances(String organizationId);

	String getAssessmentId(String selectedVal);

	List<Entry<String, Domain>> getComplianceDefinitionDetails(String complianceName);

	List<Control> getControls(String complianceName);

	void saveQuestions(List<Questions> questionsList);

	void saveQuestions(String controlLabel, String questionCode, String question);

	String saveComplianceDefinitionData(String complianceName, List<Domain> domains);

	Boolean isExistsAssessmentId(String assessmentId);

	Integer updateControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2, String assessmentId);

	ControlEffectiveness geControlEffectivenessDataForControl(String controlCode, String assessmentId);

	Boolean checkIfControlExists(String controlCode,String assessmentId);

	List<Entry<String, Domain>> getExistingComplianceDetails(String complianceId);

	UploadFile getUploadedFile(String filename, String assessmentId);

	String enterMaturityDefinitionValues(String complianceId, String rangeFrom, String rangeTo);

	List<User> getUsersInOrg(String orgId, Integer userId);

	Integer createNewUser(User userDetails);

	Integer updateUserDetails(User userDetails);

	Boolean checkAdminGenPassword(String userId, String password);

	String getUserId(String username);

	String saveChangedPasswordDetails(ValidityObj validityObj);

	String getSecurityQuestion(String username);

	Boolean verifyAnswer(String username, String answer);

	String savePassword(String pwd, String username);
}
