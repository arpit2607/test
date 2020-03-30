package com.sisai.mynewapp.config;


import javax.jms.Topic;  
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@EnableJms
@Configuration
public class Config {
	
	@Value("${activemq.broker-url}")
	private String brokerUrl;
	
	@Bean
	public Topic topic() {
		return new ActiveMQTopic("STVOM-4f7788");
	}
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory= new ActiveMQConnectionFactory();
		factory.setBrokerURL(brokerUrl);
		return factory;
	}
	
	@Bean
	public JmsTemplate jmstemplate() {
		JmsTemplate jmstemplate=new JmsTemplate();
		jmstemplate.setPubSubDomain(true);
		return new JmsTemplate(activeMQConnectionFactory());
	}
		
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(activeMQConnectionFactory());
	    factory.setConcurrency("1-1");
	    factory.setPubSubDomain(true);
	    return factory;
	}
}
