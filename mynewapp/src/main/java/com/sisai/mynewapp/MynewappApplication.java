package com.sisai.mynewapp;

import org.springframework.boot.SpringApplication;  

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;

import com.sisai.mynewapp.listener.Subscriber;

@SpringBootApplication (exclude= {DataSourceAutoConfiguration.class})
@EnableJms
public class MynewappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MynewappApplication.class, args);
		/*Subscriber s =new Subscriber();
		try {
			s.receive();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
