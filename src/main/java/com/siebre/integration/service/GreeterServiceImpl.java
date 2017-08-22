package com.siebre.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service("greeterService")
public class GreeterServiceImpl implements GreeterService {

	@Autowired
	private MessageChannel helloWorldChannel;
	
	@Override
	public void greet(String name) {
		helloWorldChannel.send(MessageBuilder.withPayload(name).build());
	}
	
}
