package com.se.compsecure.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.se.compsecure.model.MaturityEffectiveness;

@Controller
public class MaturityEffectivenessController {

	@RequestMapping(value="/saveMaturityEffectiveness",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveMaturityEffectivenessDetails(@RequestBody List<MaturityEffectiveness> maturityEffectiveness){
		return "completed";
	}
}
