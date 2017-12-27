package com.se.compsecure.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;
import com.se.compsecure.utility.ComplianceDefUtil;

@Controller
public class ComplianceController {
	
	private static final Logger LOGGER = Logger.getLogger(ComplianceController.class.getName());

	@Autowired
	private CompSecureService compSecureService;
	
	@RequestMapping(value="/enterComplianceDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String enterComplianceDetails(Model model,HttpSession httpSession,
    					@RequestBody ComplianceHeader complianceHeader) {
		       
		LOGGER.info("***************** INSIDE ENTER COMPLIANCE DETAILS **********************");
		LOGGER.info(complianceHeader.getComplianceCode() + complianceHeader.getComplianceDescription());
		
		User user = (User)httpSession.getAttribute("user");
		//TODO : Add provision to update the correct organization id.
		compSecureService.createCompliance(complianceHeader);
		
        return "compliance_definition_add";
    }
	
	
	
//	@RequestMapping(value="/saveComplianceDefData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public String saveComplianceDefData(Model model,HttpSession httpSession,
//    					@RequestBody ComplianceHeader complianceHeader) {
//		       
//		LOGGER.info("***************** INSIDE saveComplianceDefData **********************");
//		LOGGER.info(complianceHeader.getComplianceCode() + complianceHeader.getComplianceDescription());
//		
//		LOGGER.info("Getting the compliance id from the compliance description");
//		String complianceId = compSecureService.getComplianceId(complianceHeader.getComplianceName());
//		
//		User user = (User)httpSession.getAttribute("user");
//		
//		// map the compliance name with the id for matching in the database
//		complianceHeader.setComplianceId(Integer.valueOf(complianceId));
//		
//		compSecureService.saveComplianceDefinitionData(complianceHeader);
//		
//        return "questions_add";
//    }
	
	@RequestMapping(value="/saveComplianceDefData", method = RequestMethod.POST)
	@ResponseBody
    public String saveComplianceDefData(@RequestParam(value="complianceName") String complianceName,
    							@RequestParam(value="formData") String domainDetails,HttpSession httpSession) {
		       
		LOGGER.info("***************** INSIDE saveComplianceDefData **********************");
		LOGGER.info("***************** COMPLIANCE NAME :  ********************** : " + complianceName.toUpperCase());
		
		Gson gson = new Gson();
		
		String domainJson = ComplianceDefUtil.generateKeyValPair(domainDetails);
		
		
		List<Domain> domains = gson.fromJson(domainJson, new TypeToken<List<Domain>>(){}.getType());
		
		String outcome = compSecureService.saveComplianceDefinitionData(complianceName,domains);
			
		System.out.println(domains.size());
		
//		LOGGER.info(complianceHeader.getComplianceCode() + complianceHeader.getComplianceDescription());
//		
//		LOGGER.info("Getting the compliance id from the compliance description");
//		String complianceId = compSecureService.getComplianceId(complianceHeader.getComplianceName());
//		
//		User user = (User)httpSession.getAttribute("user");
//		
//		// map the compliance name with the id for matching in the database
//		complianceHeader.setComplianceId(Integer.valueOf(complianceId));
//		
//		compSecureService.saveComplianceDefinitionData(complianceHeader);
		
		
        return gson.toJson(outcome);
    }
	
	
	@RequestMapping(value="/enterMaturityDefinitionValues", method = RequestMethod.POST)
	@ResponseBody
    public String enterMaturityDefinitionValues(@RequestParam(value="complianceName") String complianceName,
    							@RequestParam(value="rangeFrom") String rangeFrom,
    							@RequestParam(value="rangeTo") String rangeTo,HttpSession httpSession) {
		
		Gson gson = new Gson();
		       
		LOGGER.info("***************** INSIDE enterMaturityDefinitionValues **********************");
		LOGGER.info("***************** COMPLIANCE NAME :  ********************** : " + complianceName.toUpperCase());
		
		String complianceId = compSecureService.getComplianceId(complianceName);
		
		String outcome = compSecureService.enterMaturityDefinitionValues(complianceId,rangeFrom,rangeTo);
		
        return gson.toJson(outcome);
    }
	
	@RequestMapping(value="/getMaturityLevels")
	@ResponseBody
    public String getMaturityLevels(@RequestParam(value="complianceName") String complianceName,HttpSession httpSession) {
		
		       
		LOGGER.info("***************** INSIDE enterMaturityDefinitionValues **********************");
		LOGGER.info("***************** COMPLIANCE ID :  ********************** : " + complianceName.toUpperCase());
		
		String complianceId = compSecureService.getComplianceId(complianceName);
		
		String [] levels = compSecureService.getMaturityDetails(complianceId);
		
		if(levels.length>0){		
			int from = Integer.parseInt(levels[0]);
			int to = Integer.parseInt(levels[1]);
			
			String options = "";
			
			for(int i=from;i<=to;i++){
				options = options+"<option>"+i+"</option>";
			}
			
	        return ("<options>"+options+"</options>");
		}
		else return "";
    }
	
}
