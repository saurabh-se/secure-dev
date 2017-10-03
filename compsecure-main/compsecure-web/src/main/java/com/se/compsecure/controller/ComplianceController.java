package com.se.compsecure.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;

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
	
	@RequestMapping(value="/saveComplianceDefData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveComplianceDefData(Model model,HttpSession httpSession,
    					@RequestBody ComplianceHeader complianceHeader) {
		       
		LOGGER.info("***************** INSIDE saveComplianceDefData **********************");
		LOGGER.info(complianceHeader.getComplianceCode() + complianceHeader.getComplianceDescription());
		
		LOGGER.info("Getting the compliance id from the compliance description");
		String complianceId = compSecureService.getComplianceId(complianceHeader.getComplianceName());
		
		User user = (User)httpSession.getAttribute("user");
		
		// map the compliance name with the id for matching in the database
		complianceHeader.setComplianceId(Integer.valueOf(complianceId));
		
		compSecureService.saveComplianceDefinitionData(complianceHeader);
		
        return "questions_add";
    }
	
}
