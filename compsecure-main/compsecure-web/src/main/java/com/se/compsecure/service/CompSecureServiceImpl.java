package com.se.compsecure.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.se.compsecure.dao.CompSecureDAO;
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
public class CompSecureServiceImpl implements CompSecureService {
	
	private final Logger LOGGER = Logger.getLogger(CompSecureServiceImpl.class.getName());
	
	@Autowired
	CompSecureDAO compSecureDAO;
	
	@Autowired
	MasterAssessmentDetails masterAssessmentDetails;
	
	// 1. Get the assessment details 
	public List<AssessmentDetails> getAssessmentDetails(String organizationId) {
		
		List<AssessmentDetails> assessmentDetails = compSecureDAO.getAssessmentDetails(organizationId);
		for (Iterator iterator = assessmentDetails.iterator(); iterator.hasNext();) {
			AssessmentDetails assessmentDetails2 = (AssessmentDetails) iterator.next();
			System.out.println("assessment_from_date " +assessmentDetails2.getAssessmentStartDate());
			System.out.println("assessment_to_date " + assessmentDetails2.getAssessmentToDate());
			
		}
		return assessmentDetails;
	}

	public List<ComplianceHeader> getComplianceDetails(String assessmentId) {
		return compSecureDAO.getComplianceDetails(assessmentId);
	}
	
	public List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceId) {
		return compSecureDAO.getDomainDetailsForCompliance(complianceId);
	}

	// 2. Get the questionnaire for that assessment
	public List<Questionnaire> getQuestionnaire(Integer levelHeaderId) {
		return null;
	}

	// 3. Get the Control Effectiveness details
	public List<Control> getControlDetails(Integer levelHeaderId) {
		return null;
	}


	// this method has to populate the assessment details and then return to the control
	public MasterAssessmentDetails getMasterAssessmentDetails(String organizationId) {
		
		List<AssessmentDetails> assessmentDetails = getAssessmentDetails(organizationId);
//		List<Questionnaire> questionnaireList = getQuestionnaire(Integer.valueOf(assessmentDetails.getOrganizationId()));
//		List<Control> controlsList = getControlDetails(Integer.valueOf(assessmentDetails.getOrganizationId()));
		
		
		return null;
	}

	public List<OrganizationDetails> getOrganizationList() {
		LOGGER.info("inside the getOrganizationList");
		List<OrganizationDetails> orgList = compSecureDAO.getOrganizationList();
		LOGGER.info(" Returned  : " + orgList.size());
		return orgList;
	}
	
	public List<OrganizationDetails> getOrganizationList(String userId) {
		LOGGER.info("inside the getOrganizationList with userId");
		return compSecureDAO.getOrganizationList(userId);
	}
	

	/**
	 * TODO : Change complianceId to compliance Name wherever necessary!!
	 */
	public List<Entry<String , Domain>> getDomainDetails(String assessmentId,String complianceId) {

		// get all the domain related data and return a list.
		List<Entry<String , Domain>> domainDetailsList = new ArrayList<Map.Entry<String,Domain>>();
		
		domainDetailsList = compSecureDAO.getDomainDetails(assessmentId,complianceId);
				
		return domainDetailsList;
	}

	public List<Questions> getQuestions(String controlCode) {
		List<Questions> questionsList = compSecureDAO.getQuestions(controlCode);
		return questionsList;
	}

	public List<Questions> getComplianceQuestions(String complianceName,String assessmentId) {
		List<Questions> complianceQuestionsList = compSecureDAO.getComplianceQuestions(complianceName,assessmentId);
		return complianceQuestionsList;
	}

	public Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes,String assessmentId) {

		return compSecureDAO.saveComplianceQuestionsResponse(questRes,assessmentId);
	}

	public User authenticate(User user,String salt) {
		
		Boolean validationRes = false;
		
		User authenticatedUser = compSecureDAO.authenticateUser(user,salt);
		
		return authenticatedUser;
	}

	public UserRoles getRole(User user) {
		return compSecureDAO.getRole(user);
	}

	public void save(MultipartFile uploadFile,String docToUpload,String assessmentId,String controlCode) throws IOException {
		
		UploadFile file = new UploadFile();
		file.setFileName(uploadFile.getOriginalFilename());
		file.setData(uploadFile.getBytes());
		
		String contentType = "txt";
		
	      String fileName = uploadFile.getOriginalFilename().toString();
	      if(fileName.contains(".")){
	    	  contentType = fileName.substring(fileName.indexOf("."));
	      }
          
          LOGGER.info("********************************");
          LOGGER.info(contentType);
		
		file.setContentType(contentType);
		file.setAssessmentId(assessmentId);
		file.setControlCode(controlCode);
		
		compSecureDAO.uploadFile(file,docToUpload);
	}

	public void saveControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2,String assessmentId) {
			compSecureDAO.saveControlEffectivenessDetails(controlEffectiveness2,assessmentId);
	}

	public List<Questions> getQuestions(String controlCode, String assessmentId) {
		return compSecureDAO.getQuestions(controlCode,assessmentId);
	}

	public List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId) {
		return compSecureDAO.getComplianceDetailsForOrg(organizationId);
	}

	public Integer alterComplianceQuestionsResponse(List<QuestionsResponse> questionResponseList) {
		return compSecureDAO.alterComplianceQuestionsResponse(questionResponseList);
	}

	public void createCompliance(ComplianceHeader complianceHeader) {
		 compSecureDAO.createCompliance(complianceHeader);		
	}

	public String saveAssessmentDetails(AssessmentDetails assessmentDetails,String self_assessment_option) {
		return compSecureDAO.saveAssessmentDetails(assessmentDetails,self_assessment_option);
	}

	public List<Questions> getComplianceQuestionsForExistingAssessment(String assessmentId) {
		return compSecureDAO.getComplianceQuestionsForExistingAssessment(assessmentId);
	}

	public List<Entry<String, Domain>> getCompleteDetails(String assessmentId, String complianceId) {
		return compSecureDAO.getCompleteDetails(assessmentId,complianceId);
	}

	public List<ControlEffectiveness> getControlEffectivenessDetails(String assessmentId, String complianceDesc) {
		return compSecureDAO.getControlEffectivenessDetails(assessmentId, complianceDesc);
	}

	public String getComplianceId(String complianceDescription) {
		return compSecureDAO.getComplianceId(complianceDescription);
	}

	// Add domain details
	// add subdomain details
	// add control details
	public void saveComplianceDefinitionData(ComplianceHeader complianceHeader) {
		List<Domain> domains = complianceHeader.getDomains();
		System.out.println(domains);
		for (Iterator iterator = domains.iterator(); iterator.hasNext();) {
			Domain domain = (Domain) iterator.next();
			String domainId = compSecureDAO.addDomain(domain,complianceHeader.getComplianceId());
			List<Subdomain> subdomains = domain.getSubdomain();
			for (Iterator iterator2 = subdomains.iterator(); iterator2.hasNext();) {
				Subdomain subdomain = (Subdomain) iterator2.next();
				String subdomainId = compSecureDAO.addSubdomain(subdomain,domainId);
				List<Control> controls = subdomain.getControl();
				for (Iterator iterator3 = controls.iterator(); iterator3.hasNext();) {
					Control control = (Control) iterator3.next();
					compSecureDAO.addControl(control,subdomainId);
				}
			}
		}
		compSecureDAO.saveComplianceDefinitionData(complianceHeader);
	}

	public Map<String, String> getCompliances(String organizationId) {
		return compSecureDAO.getCompliances(organizationId);
	}

	public String getAssessmentId(String complianceDesc) {
		return compSecureDAO.getAssessmentId(complianceDesc);
	}

	public List<Entry<String, Domain>> getComplianceDefinitionDetails(String complianceName) {
		
		return compSecureDAO.getComlianceDefinitionDetails(complianceName);
	}

	public List<Control> getControls(String complianceName) {
		return compSecureDAO.getControlsForQuestions(complianceName);
	}

	public void saveQuestions(List<Questions> questionsList) {
		compSecureDAO.saveQuestions(questionsList);
		
	}

	public void saveQuestions(String controlLabel, String questionCode, String question) {
		compSecureDAO.saveQuestions(controlLabel,questionCode,question);
	}

	public String saveComplianceDefinitionData(String complianceName,List<Domain> domains) {
		return compSecureDAO.saveComplianceDefinitionData(complianceName,domains);
	}

	public Boolean isExistsAssessmentId(String assessmentId) {
		return compSecureDAO.doesAssessmentIdExist(assessmentId);
	}

	public Integer updateControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2, String assessmentId) {
		return compSecureDAO.updateControlEffectivenessDetails(controlEffectiveness2, assessmentId);
	}

	public ControlEffectiveness geControlEffectivenessDataForControl(String controlCode, String assessmentId) {
		return compSecureDAO.geControlEffectivenessDataForControl(controlCode,assessmentId);
	}

	public Boolean checkIfControlExists(String controlCode,String assessmentId) {
		return compSecureDAO.checkIfControlExists(controlCode,assessmentId);
	}

	public List<Entry<String, Domain>> getExistingComplianceDetails(String complianceName) {
		return compSecureDAO.getExistingComplianceDetails(complianceName);
	}

	public UploadFile getUploadedFile(String filename, String assessmentId) {
		
		return compSecureDAO.getFile(filename,assessmentId);
	}

	@Override
	public String enterMaturityDefinitionValues(String complianceId, String rangeFrom, String rangeTo) {
		return compSecureDAO.enterMaturityDefinitionValues(complianceId,rangeFrom,rangeTo);
	}

	@Override
	public String[] getMaturityDetails(String complianceId) {
		return compSecureDAO.getMaturityLevels(complianceId);
	}
	
	@Override
	public List<User> getUsersInOrg(String orgId,Integer userId) {
		return compSecureDAO.getUsersInOrg(orgId,userId);
	}
	
	@Override
	public Integer createNewUser(User userDetails) {
		return compSecureDAO.createNewUser(userDetails);
	}
	
	@Override
	public Integer updateUserDetails(User userDetails) {
		return compSecureDAO.updateUserDetails(userDetails);
	}
	
	@Override
	public Boolean checkAdminGenPassword(String userId, String password) {
		return compSecureDAO.checkAdminGenPassword(userId,password);
	}
	
	@Override
	public String getUserId(String username) {
		return compSecureDAO.getUserId(username);
	}
	
	@Override
	public String saveChangedPasswordDetails(ValidityObj validityObj) {
		return compSecureDAO.saveChangedPasswordDetails(validityObj);
	}
	
	@Override
	public String getSecurityQuestion(String username) {
		return compSecureDAO.getSecurityQuestion(username);
	}

	@Override
	public Boolean verifyAnswer(String username, String answer) {
		return compSecureDAO.verifyAnswer(username,answer);
	}

	@Override
	public String savePassword(String pwd, String username) {
		return compSecureDAO.savePassword(pwd,username);
	}
}
