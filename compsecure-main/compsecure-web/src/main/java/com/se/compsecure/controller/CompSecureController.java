package com.se.compsecure.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.se.compsecure.model.OrganizationDetails;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;

@Controller
public class CompSecureController {
	
	private static final Logger LOGGER = Logger.getLogger(CompSecureController.class.getName());

	private static final String SELF_ASSESSMENT_OPTION = "self_assessment_option";
	private static final Object SELF_ASSESSMENT_OPTION_NEW = "new";
	private static final Object SELF_ASSESSMENT_OPTION_EXISTING = "existing";
	
	
	private final Integer  ORG_LIST_ZERO_ID  = 0;
	private final String ORG_LIST_ZERO_VALUE = "Please Select";
	
	@Autowired
	private CompSecureService compSecureService;
	
	@RequestMapping("/hello")
    public String hello(Model model) {
        
        model.addAttribute("greeting", "Hello Spring MVC");
        return "login";
    }
	
	@RequestMapping(value={"/self-assessment_1","/home"})
    public String getSelfAssessment(@ModelAttribute User user) {
		
		System.out.println(user.getUsername());
        
        return "self-assessment_1";
    }
	
	@RequestMapping("/self-assessment")
    public String getSelfAssessmentPage(Model model,HttpSession httpSession) {
        
        System.out.println("Inside Self Assessment New");
        
        httpSession.setAttribute(SELF_ASSESSMENT_OPTION, SELF_ASSESSMENT_OPTION_NEW);
        
        return "self-assessment";
    }
	
	@RequestMapping("/self-assessment-existing")
    public String getSelfAssessmentExistingPage(Model model,HttpSession httpSession) {
        
        System.out.println("Inside Self Assessment Existing");
        
        httpSession.setAttribute(SELF_ASSESSMENT_OPTION, SELF_ASSESSMENT_OPTION_EXISTING);
        
        return "self-assessment";
    }
	
	@RequestMapping("/questionnaire")
    public String getQuestionnairePage(Model model) {
        
        model.addAttribute("greeting", "Hello Spring MVC");
        return "questionnaire";
    }
	
	@RequestMapping("/control-effectiveness")
    public String getControlEffectivnessPage(Model model) {
        
        model.addAttribute("greeting", "Hello Spring MVC");
        return "control-effectiveness";
    }
	
	@RequestMapping("/hello1")
	@ResponseBody
    public String hello1(Model model) {
		return null;
    }
	
	@RequestMapping("/compliance-header")
    public String getComplianceHeader(Model model) {
		return "compliance-header";
    }
	@RequestMapping("/compliance_definition_add")
    public String getComplianceDefinitionAdd(Model model) {
		return "compliance_definition_add";
    }
	@RequestMapping("/maturity_definition_add")
    public String getMaturityDefinitionAdd(Model model) {
		return "maturity_definition_add";
    }
	
	@RequestMapping("/compliance_qualifier")
    public String getComplianceQualifier(Model model) {
		return "welcome";
    }
	
	
	
	@RequestMapping("/getOrgDetails/{userId}/{roleId}")
	@ResponseBody
    public String getOrgDetails(Model model,@PathVariable String userId,@PathVariable String roleId, HttpSession httpSession) {
		
		LOGGER.info("in the getOrgDetails, userId " + userId);
		
		User user = (User)httpSession.getAttribute("user");
		if(!user.equals(null)){
			System.out.println(user.getUsername() + " " + user.getUserId());
		}
		
		List<OrganizationDetails> orgList = new ArrayList<OrganizationDetails>();
		
		if(roleId.equals("1")){
			orgList = compSecureService.getOrganizationList();
		}
		else{
			orgList = compSecureService.getOrganizationList(userId);
		}
		
		Map<Integer, String> orgListKeyVal = new HashMap<Integer, String>();
		orgListKeyVal.put(ORG_LIST_ZERO_ID, ORG_LIST_ZERO_VALUE);
		
		for (Iterator iterator = orgList.iterator(); iterator.hasNext();) {
			OrganizationDetails organizationDetails = (OrganizationDetails) iterator.next();
			orgListKeyVal.put(organizationDetails.getOrganizationId(), organizationDetails.getOrganizationName());
		}
		
        Gson gson = new Gson();
		String json = gson.toJson(orgListKeyVal);
		return json;
    }
	
	@RequestMapping("/maturity-effectiveness")
    public String getMaturityEffPage(Model model) {
		
		return "maturity-effectiveness";
	
    }
	
}
