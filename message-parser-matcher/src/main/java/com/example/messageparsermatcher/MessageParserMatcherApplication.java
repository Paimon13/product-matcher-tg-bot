package com.example.messageparsermatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessageParserMatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageParserMatcherApplication.class, args);
	}

}
