package com.se.compsecure.controller;


import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.se.compsecure.service.CompSecureService;

@Controller
public class ForgotPasswordController {
	
	private static final Logger LOGGER = Logger.getLogger(ForgotPasswordController.class.getName());
	
	@Autowired
	CompSecureService compsecureService;
	
	@RequestMapping("/forgotPassword")
	public String forgotPassword(HttpSession httpSession){
		LOGGER.info("In the forgotPassword method!!");
		String nextPage = (String)httpSession.getAttribute("nextpage");
		if(nextPage!=null && nextPage.equals("login")){
			return "login";
		}
		return "forgot-password";
	}
	
	@RequestMapping("/getSecurityQuestions")
	@ResponseBody
	public String getSecurityQuestions(HttpSession httpSession, String username){
		String securityQuestion = compsecureService.getSecurityQuestion(username);
		httpSession.setAttribute("username", username);
		return securityQuestion;
	}
	
	@RequestMapping("/verifySecurityAnswer")
	@ResponseBody
	public Boolean verifyAnswer(HttpSession httpSession,@RequestParam("answer") String answer){
		String username = (String)httpSession.getAttribute("username");
		return compsecureService.verifyAnswer(username,answer);
	}
	
	@RequestMapping(value="/savePassword",method=RequestMethod.POST)
	@ResponseBody
	public String savePassword(HttpSession httpSession,@RequestParam("pwd") String pwd){
		String result = compsecureService.savePassword(pwd,(String)httpSession.getAttribute("username"));
		if(result!=null){
			httpSession.setAttribute("nextpage", "login");
			return result;
		}else
			return null;
	}
}
