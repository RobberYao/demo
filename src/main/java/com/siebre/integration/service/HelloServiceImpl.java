package com.siebre.integration.service;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService {

	@Override
	@ServiceActivator
	public void hello(String name) {
		System.err.println("hello," + name);
	}
	
}
