package com.se.compsecure.controller;

import java.util.Calendar;
import java.util.Date;
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
import com.se.compsecure.model.OrganizationDetails;
import com.se.compsecure.model.User;
import com.se.compsecure.service.CompSecureService;
import com.se.compsecure.utility.CompSecureConstants;
import com.se.compsecure.utility.EmailUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
@RequestMapping("ad")
public class AdminController {

	private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
	
	@Autowired
	private CompSecureService compSecureService;
	
	
	@RequestMapping(value="/getUsers")
	@ResponseBody
	public String getUsers(HttpSession httpSession){
		
		Gson gson = new Gson();
		List<User> userList = null;
		
		User user = (User)httpSession.getAttribute("user");
		Integer userRole = user.getRole().getRoleId();
		
		if(userRole==1){
			String orgId = user.getOrganizationId();
			Integer userId = user.getUserId();
			userList = compSecureService.getUsersInOrg(orgId,userId);
		}
		
		return gson.toJson(userList);
		
	}
	
	@RequestMapping(value="/saveNewUserDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveNewUserDetails(@RequestBody User userDetails,HttpSession httpSession){
		
		LOGGER.info("Form Data : " + userDetails.toString());
		
		Gson gson = new Gson();
		List<User> userList = null;
		
		User user = (User)httpSession.getAttribute("user");
		Integer updatedRecords = compSecureService.createNewUser(userDetails);
		if(updatedRecords!=0){
			String message = createChangePasswordMessage(userDetails.getUsername(),userDetails.getUserId().toString());
			EmailUtil.sendMail(userDetails.getEmailId(),userDetails.getUsername(),message);
		}
		
		return gson.toJson(userList);
		
	}
	
	@RequestMapping(value="/reactivateUsers",method = RequestMethod.POST)
	@ResponseBody
	public String reactivateUsers(@RequestParam("username") String username, @RequestParam("email") String emailId,HttpSession httpSession){
		
		Gson gson = new Gson();
		List<User> userList = null;
		
		LOGGER.info("Form Data : " + username + " " + emailId);
		
		String userId = compSecureService.getUserId(username);
		if(userId==null){
			return "";
		}
		
		User user = (User)httpSession.getAttribute("user");
		String message = createChangePasswordMessage(username,userId);
		EmailUtil.sendMail(emailId,username,message);
		
		return gson.toJson(userList);
	}
	

	private String createChangePasswordMessage(String username,String userId) {

		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 15);

		String token = Jwts.builder().setSubject(username).claim("roles", userId)
				.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").setExpiration(now.getTime())
				.compact();
		String body = "<br><a href='http://localhost:8080/compsecure-web/change-password?data="+ token +"'> Change Password </a>";

		return body;
	}

	@RequestMapping(value="/updateUserDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateUserDetails(@RequestBody User userDetails,HttpSession httpSession){
		
		LOGGER.info("Form Data : " + userDetails.toString());
		
		Gson gson = new Gson();
		List<User> userList = null;
		
		User user = (User)httpSession.getAttribute("user");
//		Integer userRole = user.getRole().getRoleId();
//		
//		if(userRole==1){
			String orgId = user.getOrganizationId();
			Integer userId = user.getUserId();
//			userList = compSecureService.getUsersInOrg(orgId,userId);
			Integer updatedRecords = compSecureService.updateUserDetails(userDetails);
//		}
		
		return gson.toJson(userList);
		
	}
	
	@RequestMapping(value="/getOrgList",method = RequestMethod.GET)
	@ResponseBody
	public String getOrgList(HttpSession httpSession){
		
		System.out.println("inside getOrgList");
		
		User user = (User)httpSession.getAttribute("user"); 
		if(user==null || !CompSecureConstants.ADMIN_ROLE_ID.equals(user.getRole().getRoleId().toString())){
			return null;
		}
		
		Integer userId = user.getUserId();
		
		Gson gson = new Gson();
		
		List<OrganizationDetails> organizationDetails = compSecureService.getOrganizationList();
		
		System.out.println(gson.toJson(organizationDetails));
		
		return gson.toJson(organizationDetails);
	}
}
