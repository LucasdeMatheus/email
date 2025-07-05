package com.myproject.sendEmails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SendEmailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendEmailsApplication.class, args);
	}

}
