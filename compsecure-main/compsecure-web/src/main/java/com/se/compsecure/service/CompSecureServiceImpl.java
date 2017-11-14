package com.se.compsecure.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

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

	// 4. Get the Maturity Effectiveness details
	public List<Maturity> getMaturityDetails(Integer levelHeaderId) {
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

	public User authenticate(User user) {
		
		Boolean validationRes = false;
		
		User authenticatedUser = compSecureDAO.authenticateUser(user);
		return authenticatedUser;
	}

	public UserRoles getRole(User user) {
		return compSecureDAO.getRole(user);
	}

	public List<OrganizationDetails> getOrganizationList(String userId) {
		return compSecureDAO.getOrganizationList(userId);
	}

	public String getOrganizationBasedOnLogin(String userId) {
		// TODO Auto-generated method stub
		return null;
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

	public String saveAssessmentDetails(AssessmentDetails assessmentDetails) {
		return compSecureDAO.saveAssessmentDetails(assessmentDetails);
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

	public void saveComplianceDefinitionData(String complianceName,List<Domain> domains) {
		compSecureDAO.saveComplianceDefinitionData(complianceName,domains);
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

	public Boolean checkIfControlExists(String controlCode) {
		return compSecureDAO.checkIfControlExists(controlCode);
	}
}
