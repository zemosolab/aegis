package com.aegis.cardmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CardmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardmanagementApplication.class, args);
	}

}
