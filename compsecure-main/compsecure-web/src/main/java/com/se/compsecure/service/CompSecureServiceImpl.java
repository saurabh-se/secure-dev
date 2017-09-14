package com.se.compsecure.service;

import java.io.IOException;
import java.util.ArrayList;
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
		return assessmentDetails;
	}

	public List<ComplianceHeader> getComplianceDetails(String assessmentId) {
		return compSecureDAO.getComplianceDetails(assessmentId);
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

	public List<Entry<String , Domain>> getDomainDetails(String assessmentId) {

		// get all the domain related data and return a list.
		List<Entry<String , Domain>> domainDetailsList = new ArrayList<Map.Entry<String,Domain>>();
		
		domainDetailsList = compSecureDAO.getDomainDetails(assessmentId);
		
//		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
//			Domain domain = (Domain) iterator.next();
//			System.out.println(domain.getDomainCode());
//			System.out.println(domain.getSubdomain());
//			System.out.println(domain.getSubdomain().getControl());
//		}
//		
//		
//		Map<String, Domain> domainMap = new HashMap<String, Domain>();
//		Map<String, Control> subdomainMap = new HashMap<String, Control>();
//		
//		
//		for(Domain domain : domainDetailsList) {
//			domainMap.put(domain.getDomainCode(), domain);
//		}
//		
//		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
//			Domain domain = (Domain)iterator.next();
//			Subdomain subdomain = domain.getSubdomain();
//			subdomainMap.put(subdomain.getSubdomainCode(), subdomain.getControl());
//		}
//		
//		System.out.println("// for domains");
//		for (Map.Entry<String, Domain> entry : domainMap.entrySet()) {
//			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
//			
//		}
//		
//		
//		System.out.println("// for subdomains");
//		for (Map.Entry<String, Control> entry : subdomainMap.entrySet()) {
//			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue().getControlCode());
//			
//		}
		
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

	public Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes) {

		return compSecureDAO.saveComplianceQuestionsResponse(questRes);
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

	public void save(MultipartFile uploadFile,String docToUpload,Integer assessmentId,String controlCode) throws IOException {
		
		UploadFile file = new UploadFile();
		file.setFileName(uploadFile.getOriginalFilename());
		file.setData(uploadFile.getBytes());
		
	      String fileName = uploadFile.getOriginalFilename().toString();
          String contentType = fileName.substring(fileName.indexOf("."));
          
          LOGGER.info("********************************");
          LOGGER.info(contentType);
		
		file.setContentType(contentType);
		file.setAssessmentId(assessmentId);
		file.setControlCode(controlCode);
		
		compSecureDAO.uploadFile(file,docToUpload);
	}

	public void saveControlEffectivenessDetails(ControlEffectiveness controlEffectiveness2) {
			compSecureDAO.saveControlEffectivenessDetails(controlEffectiveness2);
	}

	public List<Questions> getQuestions(String controlCode, String assessmentId) {
		return compSecureDAO.getQuestions(controlCode,assessmentId);
	}

	public List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId) {
		return compSecureDAO.getComplianceDetailsForOrg(organizationId);
	}

}
