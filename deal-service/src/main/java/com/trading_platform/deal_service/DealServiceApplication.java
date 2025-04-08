package com.trading_platform.deal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DealServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealServiceApplication.class, args);
	}

}
