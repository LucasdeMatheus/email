package com.example.sendEmails;

import com.example.sendEmails.Email.EmailConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SendEmailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendEmailsApplication.class, args);
	}

}
