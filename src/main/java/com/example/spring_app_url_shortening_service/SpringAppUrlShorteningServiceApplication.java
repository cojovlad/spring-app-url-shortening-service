package com.example.spring_app_url_shortening_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAppUrlShorteningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAppUrlShorteningServiceApplication.class, args);
	}

}
