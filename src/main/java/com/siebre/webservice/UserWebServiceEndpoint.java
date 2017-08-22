package com.siebre.webservice;

import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.springframework.beans.factory.annotation.Autowired;

import com.siebre.domain.User;
import com.siebre.service.UserService;

@WebService  
@SOAPBinding(style = Style.RPC)
public class UserWebServiceEndpoint {

	@Autowired
	private UserService userService;
	
	public User getUserById(String id) {
		return userService.getUserById(id);
	}
	
	public List<User> findAll() {
		return userService.findAll();
	}
	
}
