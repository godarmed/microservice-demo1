package com.godarmed.microservice.providerdemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {"com.godarmed.core.starters.*","com.godarmed.microservice.providerdemo1"})
@EnableEurekaClient
public class Providerdemo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Providerdemo1Application.class, args);
	}

}
