package com.siebre.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siebre.domain.User;
import com.siebre.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/showUser")
	public String findAll(Model model) {
		List<User> userList = userService.findAll();
		model.addAttribute(userList);
		return "showUser";
	}
	
	@RequestMapping("/user/delUser")
	public String delUser(@RequestParam("id") String id) {
		userService.deleteUserById(id);
		return "redirect:/user/showUser.action";
	}

	@RequestMapping("/hello") 
	@ResponseBody
	public String hello(@RequestBody String body) {
		return "hello, " + body;
	}
	
}
