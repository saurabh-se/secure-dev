package com.se.compsecure.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.se.compsecure.model.AssessmentDetails;
import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.ControlEffectiveness;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.QuestionsResponse;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;
import com.se.compsecure.utility.CompSecureUtil;
import com.se.compsecure.utility.QuestionsUtil;

@Controller
@SessionAttributes("complianceId")
public class TestController {

	private static final Logger LOGGER = Logger.getLogger(TestController.class.getName());
	
	@Autowired
	private CompSecureService compSecureService;


	@RequestMapping("/getAssessmentDetails")
	@ResponseBody
	public String getAssessmentDetails(Model model, @RequestParam(value = "selected") String selectedVal,
			HttpServletRequest request, HttpSession httpSession) {

		LOGGER.info("Inside the TestController");

		User user = (User)httpSession.getAttribute("user");
		if(!user.equals(null)){
			System.out.println(user.getUsername() +" " + user.getUserId());
		}
		
		List<AssessmentDetails> assessmentDetails = compSecureService.getAssessmentDetails(selectedVal);

		Map<String, AssessmentDetails> assessmentDetailsKV = new HashMap<String, AssessmentDetails>();
		for (Iterator iterator = assessmentDetails.iterator(); iterator.hasNext();) {
			AssessmentDetails assessmentDetails2 = (AssessmentDetails) iterator.next();
			LOGGER.info("ID: " + assessmentDetails2.getAssessmentId());
			LOGGER.info("Name: " + assessmentDetails2.getAssessmentName());
			assessmentDetailsKV.put(assessmentDetails2.getAssessmentId(), assessmentDetails2);
		}

		Gson gson = new Gson();
		String assessmentList = gson.toJson(assessmentDetailsKV);

		return assessmentList;
	}

	
	@RequestMapping("/getAssessmentId")
	@ResponseBody
	public String getAssessmentId(@RequestParam(value = "selectedCompliance") String selectedVal,
			HttpServletRequest request, HttpSession httpSession) {

		LOGGER.info("Inside the TestController");

		User user = (User)httpSession.getAttribute("user");
		if(!user.equals(null)){
			System.out.println(user.getUsername() +" " + user.getUserId());
		}
		
		return compSecureService.getAssessmentId(selectedVal);

	}
	
	
	
	@RequestMapping("/getComplianceDetails")
	@ResponseBody
	public String getComplianceDetails(@RequestParam(value = "selected") String assessmentId,
			HttpSession session) {

		LOGGER.info("Inside the TestController");

		List<ComplianceHeader> complianceDetails = compSecureService.getComplianceDetails(assessmentId);

		Map<String, String> complianceDetailsKV = new HashMap<String, String>();
		for (Iterator iterator = complianceDetails.iterator(); iterator.hasNext();) {

			ComplianceHeader complianceDetail = (ComplianceHeader) iterator.next();

			LOGGER.info("Compliance Name: " + complianceDetail.getComplianceName());
			LOGGER.info("Compliance Description: " + complianceDetail.getComplianceDescription());

			complianceDetailsKV.put(complianceDetail.getComplianceName(), complianceDetail.getComplianceDescription());

		}

		Gson gson = new Gson();
		String complianceDetailsList = gson.toJson(complianceDetailsKV);

		return complianceDetailsList;
	}
	
	
	@RequestMapping("/getComplianceDetailsForOrg")
	@ResponseBody
	public String getComplianceDetailsForOrg(Model model, @RequestParam(value = "selected") String organizationId,
			HttpSession session) {

		LOGGER.info("Inside the TestController");

		List<ComplianceHeader> complianceDetails = compSecureService.getComplianceDetailsForOrg(organizationId);

		Map<String, String> complianceDetailsKV = new HashMap<String, String>();
		for (Iterator iterator = complianceDetails.iterator(); iterator.hasNext();) {

			ComplianceHeader complianceDetail = (ComplianceHeader) iterator.next();

			LOGGER.info("Compliance Name: " + complianceDetail.getComplianceName());
			LOGGER.info("Compliance Description: " + complianceDetail.getComplianceDescription());

			complianceDetailsKV.put(complianceDetail.getComplianceName(), complianceDetail.getComplianceDescription());
			model.addAttribute("complianceId", complianceDetail.getComplianceId());

		}

		Gson gson = new Gson();
		String complianceDetailsList = gson.toJson(complianceDetailsKV);

		return complianceDetailsList;
	}


	@Deprecated
	@RequestMapping("/toQuestionnaire")
	public String toQuestionnaire(ModelAndView model, @ModelAttribute(value = "selected") String complianceId) {
		LOGGER.info("Assessment Id : " + complianceId);

		Gson gson = new Gson();
		String json = gson.toJson(complianceId);

		model.addObject(complianceId);

		return "questionnaire";
	}

	@RequestMapping("/getQuestions")
	@ResponseBody
	public String getQuestions(@RequestParam(value = "controlCode") String controlCode) {

		List<Questions> questionsList = compSecureService.getQuestions(controlCode);
		Gson gson = new Gson();
		String questionsJson = gson.toJson(questionsList);
		LOGGER.info("Questions List : " + questionsJson);
		return questionsJson;
	}
	
	@RequestMapping("/getQuestionResponse")
	@ResponseBody
	public String getQuestionResponses(@RequestParam(value = "controlCode") String controlCode,@RequestParam(value="assessmentId") String assessmentId,HttpSession httpSession) {

		if(assessmentId.equals(null)||assessmentId.isEmpty()){
			assessmentId = (String)httpSession.getAttribute("assessmentId");
		}
		
		List<Questions> questionsList = compSecureService.getQuestions(controlCode,assessmentId);
		
		LOGGER.info(" Control Code " + controlCode);
		LOGGER.info(" Assessment Id " + assessmentId);
		
		LOGGER.info(" Responses returned : " + questionsList.size());
				
		Gson gson = new Gson();
		String questionsJson = gson.toJson(questionsList);
		
		LOGGER.info("Questions List : " + questionsJson);
		
		return questionsJson;
	}

	@RequestMapping("/getQuestionnaireDetails")
	@ResponseBody
	public String getQuestionnaireDetails(@RequestParam(value = "complianceId") String complianceName,
			@RequestParam(value="assessmentId") String assessmentId, HttpSession httpSession ) {

		List<Questions> complianceQuestionsList = new ArrayList<Questions>();
		String self_assessment_option = (String)httpSession.getAttribute("self_assessment_option")==null?assessmentId:(String)httpSession.getAttribute("self_assessment_option");
				
		if(assessmentId==null || assessmentId.isEmpty()){
			assessmentId = (String)httpSession.getAttribute("assessmentId");
		}
		httpSession.setAttribute("assessmentId",assessmentId);
		httpSession.setAttribute("complianceDesc", complianceName);
		
		if(self_assessment_option.equals("existing")){
			complianceQuestionsList = compSecureService.getComplianceQuestionsForExistingAssessment(assessmentId);
		}else{
			complianceQuestionsList = compSecureService.getComplianceQuestions(complianceName,assessmentId);
		}

		LOGGER.info("INSIDE the getQuestionnaire method : " + complianceName);
		
		User user = (User)httpSession.getAttribute("user");
		LOGGER.info("In the Questionnaire Details section *** " + user.getUsername() + " - " + user.getUserId());

		
		Gson gson = new Gson();
		String complianceQList = gson.toJson(complianceQuestionsList);

		return complianceQList;
	}

	@RequestMapping(value = "/saveComplianceQuestionsResponse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String saveComplianceQuestionsResponse(@RequestBody List<QuestionsResponse> questRes,HttpSession httpSession) {

		String assessmentId = (String)httpSession.getAttribute("assessmentId");
		
		if(assessmentId==null){
			return "Some problem exists with the Assessment. Please create a new assess";
		}
		
		for (Iterator iterator = questRes.iterator(); iterator.hasNext();) {
			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			LOGGER.info(questionsResponse.getControlCode());
			LOGGER.info(questionsResponse.getQuestionResponse());
			LOGGER.info(questionsResponse.getQuestionRemarks());
		}
		compSecureService.saveComplianceQuestionsResponse(questRes,assessmentId);
		return "saved";
	}

	/*
	 * TODO : write code to save data
	 */
	@RequestMapping("/saveComplianceDetails")
	public void saveComplianceData(@RequestParam("details") String complianceDetails) {
		LOGGER.info(complianceDetails);
	}

	
	@RequestMapping(value = "/saveQuestions")
	public @ResponseBody String saveQuestions(@RequestParam("details") String questions) {
		
		LOGGER.info(" In the saveQuestions method : " + questions.toString());
		Gson gson = new Gson();	
		
		CompSecureUtil compSecureUtil = new CompSecureUtil();
		
		List<QuestionsUtil> quesUtil = compSecureUtil.getKeyValuePair(questions); 

		for (Iterator iterator = quesUtil.iterator(); iterator.hasNext();) {
			QuestionsUtil questionsUtil = (QuestionsUtil) iterator.next();
			compSecureService.saveQuestions(questionsUtil.getqControlLabel(), questionsUtil.getControlQuestionCode(), questionsUtil.getControlQuestion());
		}
		
		String json = gson.toJson("success");
		return json;
	}


	@RequestMapping("/getControls")
	@ResponseBody
	public String getControls(@RequestParam("complianceName") String complianceName,HttpSession httpSession) {
		LOGGER.info("Inside getControls method");

		List<Control> controlList = new ArrayList<Control>();

		controlList = compSecureService.getControls(complianceName);
		
		Gson gson = new Gson();
		String controlJson = gson.toJson(controlList);

		for (Iterator iterator = controlList.iterator(); iterator.hasNext();) {
			Control control2 = (Control) iterator.next();
			LOGGER.info(control2.getControlCode());
		}

		return controlJson;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getDomainDetails")
	public @ResponseBody String getDomainDetails(@RequestParam("assessmentId") String assessmentId,@RequestParam("complianceId") String complianceId, HttpSession httpSession) {

		List<Entry<String, Domain>> domainDetailsList = null;
		
		String self_assessment_option = (String)httpSession.getAttribute("self_assessment_option");
		complianceId = (String)httpSession.getAttribute("complianceDesc");
		String json = null;
		
//		if(self_assessment_option.equals("new")){
//			domainDetailsList = compSecureService.getDomainDetailsForCompliance(complianceId);
			domainDetailsList = compSecureService.getDomainDetails(assessmentId,complianceId);
			
			List<Domain> domainList = new ArrayList<Domain>();

			for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
				Entry<String, Domain> entry = (Entry<String, Domain>) iterator.next();
				domainList.add(entry.getValue());
			}
			
			Gson gson = new Gson();
			json = gson.toJson(domainList);
//		}
//		else{
//		}
		return json;
	}
	
	
	/**
	 * Method to be used for getting the control-effective details of an existing compliance form.
	 * @param controlCode
	 * @param assessmentId
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value="/geControlEffectivenessDataForControl")
	public @ResponseBody String geControlEffectivenessDataForControl(@RequestParam("controlCode") String controlCode,
			@RequestParam("assessmentId") String assessmentId, HttpSession httpSession) {
		
//		List<ControlEffectiveness> ceList 
		ControlEffectiveness controlEffectiveness = compSecureService.geControlEffectivenessDataForControl(controlCode,assessmentId);
		Gson gson = new Gson();
		if(controlEffectiveness!=null){
			return gson.toJson(controlEffectiveness);
		}else{
			String retValue = gson.toJson("empty");
			System.out.println("Return Value " + retValue);
			return retValue;
		}
	}
	

	@RequestMapping(value = "/handleJson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String handleJson(@RequestBody List<QuestionsResponse> questionResponseList,
			BindingResult bindingResult,HttpSession httpSession) {

		String response = "";
		JsonResponse json = new JsonResponse();

		String assessmentId = (String)httpSession.getAttribute("assessmentId");
		String complianceName = (String)httpSession.getAttribute("complianceDesc");
		
		if(assessmentId.isEmpty()){
			assessmentId = compSecureService.getAssessmentId(complianceName);
		}
		
		for (Iterator iterator = questionResponseList.iterator(); iterator.hasNext();) {

			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			LOGGER.info("Response : " + questionsResponse.getQuestionResponse());
			LOGGER.info("Q-Code : " + questionsResponse.getQuestionCode());
			LOGGER.info("Remarks : " + questionsResponse.getQuestionRemarks());
		}

		Integer count = compSecureService.saveComplianceQuestionsResponse(questionResponseList,assessmentId);

		String countUpdated = "recordsUpdated " + count;
		json.setResult(countUpdated);
		Gson gson = new Gson();
		response = gson.toJson(json);

		LOGGER.info("questions returned : " + questionResponseList.size());

		return response;
	}
	
	
	@RequestMapping(value = "/handleAlterJson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String handleAlterJson(@RequestBody List<QuestionsResponse> questionResponseList,
			BindingResult bindingResult) {

		String response = "";
		JsonResponse json = new JsonResponse();

		LOGGER.info("****************** INSIDE ALTER JSON *********************");
		
		for (Iterator iterator = questionResponseList.iterator(); iterator.hasNext();) {

			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			LOGGER.info("Response : " + questionsResponse.getQuestionResponse());
			LOGGER.info("Q-Code : " + questionsResponse.getQuestionCode());
			LOGGER.info("Remarks : " + questionsResponse.getQuestionRemarks());
		}

		Integer count = compSecureService.alterComplianceQuestionsResponse(questionResponseList);

		String countUpdated = "recordsUpdated " + count;
		json.setResult(countUpdated);
		Gson gson = new Gson();
		response = gson.toJson(json);

		LOGGER.info("questions returned : " + questionResponseList.size());

		return response;
	}

	
	@RequestMapping(value = "/saveAssessmentDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveAssessmentDetails(@RequestBody AssessmentDetails assessmentDetails,HttpSession httpSession) {
				
		String self_assessment_option = (String)httpSession.getAttribute("self_assessment_option");
		
		String assessmentID = compSecureService.saveAssessmentDetails(assessmentDetails);
		
		httpSession.setAttribute("assessmentId", assessmentID);
		System.out.println(assessmentDetails.getAssessmentName());
		System.out.println(assessmentDetails.getAssessmentDesc());
				
		return "questionnaire";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCompleteDetails")
	public @ResponseBody String getCompleteDetails(@RequestParam("assessmentId") String assessmentId,@RequestParam("complianceId") String complianceId, HttpSession httpSession) {

		List<Entry<String, Domain>> domainDetailsList = null;
		
		
//		complianceId = (String)httpSession.getAttribute("complianceDesc");
		
		System.out.println("Compliance Id " + complianceId);
		assessmentId = (String)httpSession.getAttribute("assessmentId");
		
		domainDetailsList = compSecureService.getCompleteDetails(assessmentId,complianceId);
		
		List<Domain> domainList = new ArrayList<Domain>();

		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
			Entry<String, Domain> entry = (Entry<String, Domain>) iterator.next();
			domainList.add(entry.getValue());
		}

		Gson gson = new Gson();
		String json = gson.toJson(domainList);

		return json;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getControlEffectivenessDetails")
	public @ResponseBody String getControlEffectivenessDetails(@RequestParam("assessmentId") String assessmentId,@RequestParam("complianceId") String complianceId, HttpSession httpSession) {

		List<ControlEffectiveness> controlEffectivenessList = null;
		
		complianceId = (String)httpSession.getAttribute("complianceDesc");
		
		controlEffectivenessList = compSecureService.getControlEffectivenessDetails(assessmentId,complianceId);
		
		Gson gson = new Gson();
		String json = gson.toJson(controlEffectivenessList);

		return json;
	}
	
	@RequestMapping(value = "/authenticateRole")
	public String getRole(HttpSession httpSession){
		User user = (User)httpSession.getAttribute("user");
		if(user!=null){
			String role = user.getRole().getRoleId().toString();
			httpSession.setAttribute("role", role);
			return "";
		}else{
			return "home";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCompliances")
	public @ResponseBody String getCompliances(HttpSession httpSession){
		
		User user = (User)httpSession.getAttribute("user");
		
		String organizationId = user.getOrganizationId();
		Integer role = user.getRole().getRoleId();
		
		//String organizationId = ((User)httpSession.getAttribute("user")).getOrganizationId();
		
		if(organizationId.equals("0")){
			Map<String, String> compMap = compSecureService.getCompliances(organizationId);
			Gson gson = new Gson();
			String json =  gson.toJson(compMap);
			System.out.println(json);
			return json;
		}else{
			return "self-assessment1";
		}
	}
	
	@RequestMapping(value="/getComplianceDefinitionDetails")
	public @ResponseBody String getComplianceDefinitionDetails(@RequestParam("complianceName") String complianceName, HttpSession httpSession) {

		List<Entry<String, Domain>> domainDetailsList = null;
		
		String self_assessment_option = (String)httpSession.getAttribute("self_assessment_option");
		String assessmentId = (String)httpSession.getAttribute("assessment_id");
		
//		complianceId = (String)httpSession.getAttribute("complianceDesc");
		
		System.out.println("Compliance Id :" + complianceName);
		
		domainDetailsList = compSecureService.getComplianceDefinitionDetails(complianceName);
		
		List<Domain> domainList = new ArrayList<Domain>();

		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
			Entry<String, Domain> entry = (Entry<String, Domain>) iterator.next();
			domainList.add(entry.getValue());
		}

		Gson gson = new Gson();
		String json = gson.toJson(domainList);

		return json;
	}

}
