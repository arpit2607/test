package com.sisai.mynewapp.activemq;

import java.lang.Math; 
import java.util.Random;
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
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase; 

import org.bson.Document;  
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential; 

import org.apache.activemq.ActiveMQConnectionFactory;

public class activemq{
  public static Boolean TRANSACTIONAL = false;
  public static String TOPIC_NAME ="STVOM";
  
  public static double getInt(int min,int max) {
	  return (Math.random() * ((max - min) + 1)) + min;
  }

  public static void main(String[] args) throws JMSException {
    
	MongoClient mongo=new MongoClient("localhost",27017);
	MongoCredential credential;
	credential=MongoCredential.createCredential("siteUserAdmin", "STVOM", "password".toCharArray());
	MongoDatabase db=mongo.getDatabase("STVOM");
	MongoCollection<Document> collection= db.getCollection("sampledata");
	String host = "localhost";
    String port = "61616";
    // Creating Factory for connection
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
        "tcp://" + host + ":" + port);
    Connection connection = factory.createConnection();
    // Setting unique client id for durable subscriber
    connection.setClientID("MyListener");
    connection.start();
    double Gx,Gy,Gz,ThetaX,ThetaY,Temp;
    try {
    	while(true) {
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		
		// Publish
		Gx=getInt(-950,950);
		Gy=getInt(-250,250);
		Gz=getInt(-250,250);
		ThetaX=getInt(-90,90);
		ThetaY=getInt(-90,90);
		Temp=getInt(20,50);
		
		String payload = "{Gx:"+Gx+", Gy:"+Gy+", Gz:"+Gz+", ThetaX:"+ThetaX+", ThetaY:"+ThetaY+", Temp:"+Temp+"}";
		TextMessage msg = session.createTextMessage(payload);
		MessageProducer publisher = session.createProducer(topic);
		//System.out.println("Sending text '" + payload + "'");
		publisher.send(msg, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
		//delay(1000);
		// Consumer1 subscribes to customerTopic
		MessageConsumer consumer1 = session.createDurableSubscriber(topic, "consumer1", "", false);		

		// Consumer2 subscribes to customerTopic
		MessageConsumer consumer2 = session.createDurableSubscriber(topic, "consumer2", "", false);	

		
		
		msg = (TextMessage) consumer1.receive();
		System.out.println("Consumer1 receives " + msg.getText());
		
		
		msg = (TextMessage) consumer2.receive();
		System.out.println("Consumer2 receives " + msg.getText());
		
		if(Temp>30) {
			Document document=new Document("title", "MongoDB")
					.append("Gx", Gx)
					.append("Gy", Gy)
					.append("Gz", Gz)
					.append("ThetaX", ThetaX)
					.append("ThetaY", ThetaY)
					.append("Temp", Temp);
			collection.insertOne(document);
		}
		
		session.close();
    	}
		
    	
    }finally {
    	if (connection != null) {
			connection.close();
		}
    }
    
  	}
}
