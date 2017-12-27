package com.se.compsecure.controller;


import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.se.compsecure.service.CompSecureService;
import com.se.compsecure.utility.ValidityObj;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Controller
public class ChangePasswordController {
	
	private static final Logger LOGGER = Logger.getLogger(ChangePasswordController.class.getName());
	
	@Autowired
	CompSecureService compsecureService;
	
	@RequestMapping("/checkExpiry")
	@ResponseBody
	public String checkExpiry(HttpSession httpSession,@RequestParam("data") String token){
		boolean validity = false;
		String userId=null;
		try{
		 Claims claims = Jwts.parser()         
			       .setSigningKey(DatatypeConverter.parseBase64Binary("secretkey"))
			       .parseClaimsJws(token).getBody();
		 
		 
	    System.out.println("ID: " + claims.get("roles"));
	    System.out.println("Subject: " + claims.getSubject());
	    System.out.println("Issuer: " + claims.getIssuer());
	    System.out.println("Expiration: " + claims.getExpiration());
	    
	    userId = claims.getSubject();
	    Date expiry = claims.getExpiration();
		if(expiry.after(new Date())){
			validity = true;
		}else{
			validity = false;
		}
		}catch(ExpiredJwtException ex){
			LOGGER.info(ex.getMessage());
			validity=false;
		}catch(Exception ex){
			LOGGER.info(ex.getMessage());
		}
		
		Gson gson = new Gson();
		
		return gson.toJson(new ValidityObj(validity, userId));
		
	}
	
	@RequestMapping("/checkAdminGenPassword")
	@ResponseBody
	public Boolean checkAdminGenPassword(HttpSession httpSession,@RequestParam("userId") String userId,
			@RequestParam("pwd") String password){
		
		LOGGER.info("in the checkAdminGenPassword method");
		if(StringUtils.isEmpty(userId)){
			return false;
		}
		
		Boolean validPassword = compsecureService.checkAdminGenPassword(userId,password);
		
		LOGGER.info("Value Returned  " + validPassword);
		
		return validPassword;
	}
	
	@RequestMapping(value="/saveChangedPasswordDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveChangedPasswordDetails(@RequestBody ValidityObj validityObj,HttpSession httpSession){
		
		String updateStatus = compsecureService.saveChangedPasswordDetails(validityObj);
		return updateStatus;
	}

}
