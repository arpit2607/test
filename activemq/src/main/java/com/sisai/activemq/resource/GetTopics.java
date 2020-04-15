package com.sisai.activemq.resource;

import java.util.Set;  

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class GetTopics {
	
	@JmsListener(destination="STVOM")
	public void receive() throws NamingException, InterruptedException{
		
		
        // Create a Connection
        ActiveMQConnection connection=null;
	
		try {
			
			ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");

			connection = (ActiveMQConnection) connectionFactory.createConnection();
			connection.start();
			DestinationSource ds =connection.getDestinationSource();
			Set<ActiveMQTopic> topics=ds.getTopics();
			System.out.println("Topics:-"+topics);
			Set<ActiveMQQueue> queues=ds.getQueues();
			System.out.println("Queues:-"+queues);
			Thread.sleep(5000);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
