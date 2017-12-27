package com.se.compsecure.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.se.compsecure.model.User;
import com.se.compsecure.model.UserRoles;
import com.se.compsecure.service.CompSecureService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
public class LoginController {
	
	@Autowired
	private CompSecureService compSecureService;
	
	@RequestMapping("/")
    public String login(Model model,HttpServletRequest httpServletRequest) {
        model.addAttribute("greeting", "Hello Spring MVC");
        System.out.println(httpServletRequest.getContextPath());
        return "login";
    }
	
	
	@RequestMapping(value="/getSalt")
	@ResponseBody
	public String getSalt(HttpSession httpSession) {
		
		int workload = ThreadLocalRandom.current().nextInt(12, 32);
		String salt = BCrypt.gensalt(workload);
		
		httpSession.setAttribute("salt", salt);
		System.out.println("INITIAL SALT : " + salt);
		
//		String hashedPassword  = BCrypt.hashpw(password, salt);
//		System.out.println(hashedPassword);
		
		return salt;
		
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String authenticate(@RequestBody User user, Model model, BindingResult bindingResult,
    		HttpServletRequest request, HttpSession httpSession) {
		
		httpSession = request.getSession(false);
        
		Gson gson = new Gson();
		model.addAttribute("user", user);
        
		UserRoles role = null;
        System.out.println("inside authenticate method");
        
        System.out.println(user.getUsername());
        System.out.println("Password : " + user.getPassword());
        
        String salt = (String)httpSession.getAttribute("salt");
        System.out.println("SALT : " + salt);
        
        User authenticatedUser = compSecureService.authenticate(user,salt);
        
        if(authenticatedUser!=null){
        	role = compSecureService.getRole(authenticatedUser);
        	String roleId = role.getRoleId().toString();
        	authenticatedUser.setRole(role);
        	authenticatedUser.setPassword("");
        	
 		   Calendar now = Calendar.getInstance();
 		   now.add(Calendar.MINUTE,30);
 		   
 	        String token = Jwts.builder().setSubject(authenticatedUser.getUsername())
 	            .claim("roles",roleId).setIssuedAt(new Date())
 	            .signWith(SignatureAlgorithm.HS256, "secretkey").setExpiration(now.getTime()).compact();
 	        
 	        LoginResponse loginResponse = new LoginResponse(token, roleId,authenticatedUser.getUserId().toString());
        	
        	httpSession.setAttribute("user", authenticatedUser);
        	return gson.toJson(loginResponse);
        }
        else{
        	return gson.toJson("invalid");
        }
    }
	
	@RequestMapping(value="/getUserRole",method=RequestMethod.GET)
    public @ResponseBody String getUserRole(HttpSession httpSession) {
		
		User user = (User)httpSession.getAttribute("user");
		UserRoles roles = null;
		
		
		if(user!=null){
			roles = compSecureService.getRole(user);
		}
		if(roles!=null){
			return roles.getRoleId().toString();
		}
		
		return "failed";
	}
	
	@RequestMapping("/logout")
    public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		System.out.println("Logout called!!");
		return "logout";
	}
}
