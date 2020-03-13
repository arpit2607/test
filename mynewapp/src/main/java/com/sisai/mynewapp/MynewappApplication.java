package com.sisai.mynewapp;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (exclude= {DataSourceAutoConfiguration.class})
public class MynewappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MynewappApplication.class, args);
	}

}
