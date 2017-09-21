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
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.QuestionsResponse;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;

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

	@RequestMapping("/getComplianceDetails")
	@ResponseBody
	public String getComplianceDetails(Model model, @RequestParam(value = "selected") String assessmentId,
			HttpSession session) {

		LOGGER.info("Inside the TestController");

		List<ComplianceHeader> complianceDetails = compSecureService.getComplianceDetails(assessmentId);

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
	public String getQuestionResponses(@RequestParam(value = "controlCode") String controlCode,@RequestParam(value="assessmentId") String assessmentId) {

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

		
		String self_assessment_option = (String)httpSession.getAttribute("self_assessment_option");
		
		if(self_assessment_option.equals("new")){
			LOGGER.info("**********" + self_assessment_option);
			assessmentId = null;
		}
		
		List<Questions> complianceQuestionsList = compSecureService.getComplianceQuestions(complianceName,assessmentId);

		LOGGER.info("INSIDE the getQuestionnaire method : " + complianceName);
		
		User user = (User)httpSession.getAttribute("user");
		LOGGER.info("In the Questionnaire Details section *** " + user.getUsername() + " - " + user.getUserId());

		
		Gson gson = new Gson();
		String complianceQList = gson.toJson(complianceQuestionsList);

		return complianceQList;
	}

	@RequestMapping(value = "/saveComplianceQuestionsResponse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String saveComplianceQuestionsResponse(@RequestBody List<QuestionsResponse> questRes) {

		for (Iterator iterator = questRes.iterator(); iterator.hasNext();) {
			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			LOGGER.info(questionsResponse.getControlCode());
			LOGGER.info(questionsResponse.getQuestionResponse());
			LOGGER.info(questionsResponse.getQuestionRemarks());
		}
		compSecureService.saveComplianceQuestionsResponse(questRes);
		return "saved";
	}

	/*
	 * TODO : write code to save data
	 */
	@RequestMapping("/saveComplianceDetails")
	public void saveComplianceData(@RequestParam("details") String complianceDetails) {
		LOGGER.info(complianceDetails);
	}

	@RequestMapping("/toAddQuestions")
	public ModelAndView toAddQuestions(ModelAndView model) {
		return new ModelAndView("questions_add");
	}

	@RequestMapping(value = "/saveQuestions")
	public @ResponseBody String saveQuestions(@RequestParam("details") String questions) {
		LOGGER.info(questions);

		Gson gson = new Gson();
		String json = gson.toJson("success");

		return json;
	}

	@RequestMapping("/getControls")
	@ResponseBody
	public String getControls() {
		LOGGER.info("Inside getControls method");

		List<Control> controlList = new ArrayList<Control>();

		Control control = new Control();

		control.setControlCode("3.2.1");
		control.setControlValue(" A cyber security committee should be established and be mandated by the board.");

		controlList.add(control);

		Control control1 = new Control();
		control1.setControlCode("3.1.1-2");
		control1.setControlValue(
				"The cyber security committee should be headed by an independent senior manager from a control function. ");

		controlList.add(control1);

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
		
		if(self_assessment_option.equals("new")){
			domainDetailsList = compSecureService.getDomainDetailsForCompliance(complianceId);
		}
		else{
			domainDetailsList = compSecureService.getDomainDetails(assessmentId);
		}
		List<Domain> domainList = new ArrayList<Domain>();

		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
			Entry<String, Domain> entry = (Entry<String, Domain>) iterator.next();
			domainList.add(entry.getValue());
		}

		Gson gson = new Gson();
		String json = gson.toJson(domainList);

		return json;
	}

	/**
	 * TEST
	 * 
	 * @param s
	 * @return
	 */

	@RequestMapping(value = "/handleJson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String handleJson(@RequestBody List<QuestionsResponse> questionResponseList,
			BindingResult bindingResult) {

		String response = "";
		JsonResponse json = new JsonResponse();

		for (Iterator iterator = questionResponseList.iterator(); iterator.hasNext();) {

			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			LOGGER.info("Response : " + questionsResponse.getQuestionResponse());
			LOGGER.info("Q-Code : " + questionsResponse.getQuestionCode());
			LOGGER.info("Remarks : " + questionsResponse.getQuestionRemarks());
		}

		Integer count = compSecureService.saveComplianceQuestionsResponse(questionResponseList);

		String countUpdated = "recordsUpdated " + count;
		json.setResult(countUpdated);
		Gson gson = new Gson();
		response = gson.toJson(json);

		LOGGER.info("questions returned : " + questionResponseList.size());

		return response;
	}

	/**
	 * TEST
	 * 
	 * @param s
	 * @return
	 */
	// @RequestMapping("/handleJson")
	// public @ResponseBody String handleJson(@RequestParam("questionsResponse")
	// String questionsResponse) {
	//
	// JsonParser jsonParser = new JsonParser();
	//
	// LOGGER.info(questionsResponse);
	//
	// Object obj = jsonParser.parse(questionsResponse);
	//
	// try{
	//// JsonObject jsonObject = (JsonObject)obj;
	//
	// JsonArray jsonArray = (JsonArray)obj;
	//
	// for (JsonElement jsonElement : jsonArray) {
	// LOGGER.info(" name --> " + jsonElement.getAsString());
	// }
	//
	//
	// }catch (Exception e) {
	// LOGGER.info(e.getMessage());
	// }
	//
	//
	// return "success";
	// }

}
