package com.godarmed.springcloud.alibabademo.consumer9600;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Consumer9600Application {

	public static void main(String[] args) {
		SpringApplication.run(Consumer9600Application.class, args);
	}

}
