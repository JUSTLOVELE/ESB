package com.esb.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class Zuul_11001_StartSpringCloudApp {

	public static void main(String[] args) {
		SpringApplication.run(Zuul_11001_StartSpringCloudApp.class, args);
	}

}
