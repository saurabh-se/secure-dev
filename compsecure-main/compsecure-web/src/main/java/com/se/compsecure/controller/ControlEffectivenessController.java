package com.se.compsecure.controller;

import java.util.Iterator;
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

import com.se.compsecure.model.ControlEffectiveness;
import com.se.compsecure.service.CompSecureService;

@Controller
public class ControlEffectivenessController {

private static final Logger LOGGER = Logger.getLogger(ControlEffectivenessController.class.getName());
	
	@Autowired
	private CompSecureService compSecureService;
	
	@RequestMapping(value="/saveControlEffectiveness",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveControlEffectivenessDetails(@RequestBody List<ControlEffectiveness> controlEffectiveness, HttpSession httpSession) {
		
		String assessment_option = (String)httpSession.getAttribute("self_assessment_option");
		LOGGER.info(" ** INSIDE CONTROL EFFECTIVENESS CONTROLLER : OPTION SELECTED : - " + assessment_option );
		
		for (Iterator iterator = controlEffectiveness.iterator(); iterator.hasNext();) {
			ControlEffectiveness controlEffectiveness2 = (ControlEffectiveness) iterator.next();
				if(controlEffectiveness2.getControlCode()!=null){
				
				LOGGER.info(" ************************ ");
				LOGGER.info("Control Code : " + controlEffectiveness2.getControlCode());
				LOGGER.info("Control Code : " + controlEffectiveness2.getDocEffectiveness());
				LOGGER.info("Control Code : " + controlEffectiveness2.getDocEffRemarks());
				LOGGER.info("Control Code : " + controlEffectiveness2.getImplEffectiveness());
				LOGGER.info("Control Code : " + controlEffectiveness2.getImplEffRemarks());
				LOGGER.info("Control Code : " + controlEffectiveness2.getRecEffectiveness());
				LOGGER.info("Control Code : " + controlEffectiveness2.getRecEffRemarks());
				LOGGER.info(" ************************ ");
				
				if(assessment_option.equals("new")){
					compSecureService.saveControlEffectivenessDetails(controlEffectiveness2);
				}
				else{
					System.out.println(" TODO : ALTER THE TABLE ");
					LOGGER.info("TODO :  ALTER THE TABLE ");
				}
			}
		}
		return "maturity-effectiveness";
	}
}
