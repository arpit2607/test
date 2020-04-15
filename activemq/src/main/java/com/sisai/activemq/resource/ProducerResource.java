package com.sisai.activemq.resource;

import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Topic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping
public class ProducerResource {

	@Autowired
	JmsTemplate jmstemplate;
	
	@Autowired
	Topic topic;
	
	@GetMapping("/{message}")
	public void publish(@PathVariable("message") 
							final String message) {
		while(true) {
			jmstemplate.convertAndSend(topic,message);
		}
		//return "Published Successfully";
	}
	
}
