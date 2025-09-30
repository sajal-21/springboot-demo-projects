package com.example.chatAppLv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatAppLv3Application {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppLv3Application.class, args);
	}

}
