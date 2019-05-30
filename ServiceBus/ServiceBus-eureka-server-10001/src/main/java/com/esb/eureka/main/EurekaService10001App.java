package com.esb.eureka.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaService10001App {

	public static void main(String[] args) {
		SpringApplication.run(EurekaService10001App.class, args);
	}
}
