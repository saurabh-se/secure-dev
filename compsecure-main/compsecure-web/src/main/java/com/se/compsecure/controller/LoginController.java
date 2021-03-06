package com.se.compsecure.controller;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	
	@RequestMapping(value="/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String authenticate(@RequestBody User user, Model model, BindingResult bindingResult,
    		HttpServletRequest request, HttpSession httpSession) {
		
		httpSession = request.getSession(false);
        
		Gson gson = new Gson();
		model.addAttribute("user", user);
        
		UserRoles role = null;
        System.out.println("inside authenticate method");
        
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        
        User authenticatedUser = compSecureService.authenticate(user);
        
        if(authenticatedUser!=null){
        	role = compSecureService.getRole(authenticatedUser);
        	String roleId = role.getRoleId().toString();
        	authenticatedUser.setRole(role);
        	authenticatedUser.setPassword("");
        	
        	httpSession.setAttribute("user", authenticatedUser);
        	return gson.toJson(authenticatedUser);
        }
        else{
        	return gson.toJson("invalid");
        }
    }
	
	@RequestMapping("/logout")
    public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		System.out.println("Logout called!!");
		return "logout";
	}
}
