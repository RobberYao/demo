package com.siebre.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.siebre.integration.service.GreeterService;

@Controller
public class IntegrationController {
	
	@Autowired
	private GreeterService greeterService;
	
	@RequestMapping("/integration/test")
	public void delUser(@RequestParam("name") String name) {
		greeterService.greet(name);
	}

}
