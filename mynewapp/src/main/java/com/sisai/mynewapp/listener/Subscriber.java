package com.sisai.mynewapp.listener;

import java.util.Timer; 
import java.util.TimerTask;


import java.lang.Math; 
import java.util.Random;
import com.sisai.mynewapp.listener.Person;
import com.sisai.mynewapp.repository.StvomRepository;
import com.sisai.mynewapp.controller.StvomController;

import javax.jms.BytesMessage;
import javax.jms.Connection; 
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.util.StopWatch;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

@Component
public class Subscriber implements MessageConverter{
  public static Boolean TRANSACTIONAL = false;
  public static String TOPIC_NAME ="STVOM-4f7788";
  
  @Autowired
  private StvomRepository stvomRepository;
  @Autowired
  private StvomController stvomController;
  @Autowired
  private static JmsTemplate jmsTemplate;
  
  private static final Logger LOGGER =
	      LoggerFactory.getLogger(Subscriber.class);

	  ObjectMapper mapper;

	  public Subscriber() {
	    mapper = new ObjectMapper();
	    
	  }
	  
  
  public static double getInt(int min,int max) {
	  return (Math.random() * ((max - min) + 1)) + min;
  }

  
  @JmsListener(destination="STVOM-4f7788")
public void receive() throws Exception {
	  StopWatch watch=new StopWatch();
	  
	  
	 /* MongoClient mongo=new MongoClient("localhost",27017);
		MongoCredential credential;
		credential=MongoCredential.createCredential("siteUserAdmin", "STVOM-4f7788", "password".toCharArray());
		MongoDatabase db1=mongo.getDatabase("STVOM-4f7788");
		DB db = mongo.getDB( "STVOM-4f7788" );
		DBCollection collection= db.getCollection("sampledata");*/
	  
	  
	String host = "localhost";
    String port = "61616";
    
    ObjectMapper mapper=new ObjectMapper();
    
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
        "tcp://" + host + ":" + port);
    Connection connection = factory.createConnection();
    
    connection.setClientID("MyListener");
    connection.start();
    
    try {
    	watch.restart();
    	long start=System.currentTimeMillis();
    	long end=0;
    	long elapsed =0;
    	int count=0;
    	Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);

		
		MessageConsumer consumer1 = session.createDurableSubscriber(topic, "consumer1", "", false);		

    	while(elapsed<=10000) {
    		count=count+1;
  		  System.out.println("Count="+count);
		
		
		
		
		String message=getPayload(consumer1.receive());
		Gson g=new Gson();
		Person sub=g.fromJson(message, Person.class);
		
		System.out.println("Consumer1 receives " + sub);
		//DBObject dbObject = (DBObject)JSON.parse(message);
		 
		 stvomRepository.save(sub);
		  //collection.insert(dbObject);
		  
		  end=System.currentTimeMillis();
		  elapsed=end-start;
		
    	}
    	session.close();
    }finally {
    	if (connection != null) {
			connection.close();
		}
    }
    
  	}

  private static String getPayload(Message message) throws Exception {
		String payload = null;
		
		if (message instanceof TextMessage) {
			payload = ((TextMessage) message).getText();
		} 
		else if(message instanceof BytesMessage) {
			BytesMessage bMessage = (BytesMessage) message;
			int payloadLength = (int)bMessage.getBodyLength();
			byte payloadBytes[] = new byte[payloadLength];
			bMessage.readBytes(payloadBytes);
			payload = new String(payloadBytes);
		}
		else {
			LOGGER.warn("Message not recognized as a TextMessage or BytesMessage.  It is of type: "+message.getClass().toString());
			payload = message.toString();
		}
		return payload;
	}  
  
@Override
public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object fromMessage(Message message) throws JMSException, MessageConversionException {
	// TODO Auto-generated method stub
	TextMessage textMessage = (TextMessage) message;
    String payload = textMessage.getText();
    LOGGER.info("inbound json='{}'", payload);

    Person person = null;
    try {
      person = mapper.readValue(payload, Person.class);
    } catch (Exception e) {
      LOGGER.error("error converting to person", e);
    }

    return person;
	
}
}