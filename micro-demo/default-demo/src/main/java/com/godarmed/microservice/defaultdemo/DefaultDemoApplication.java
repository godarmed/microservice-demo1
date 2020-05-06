package com.godarmed.microservice.defaultdemo;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

@EnableAsync
@SpringBootApplication
public class DefaultDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefaultDemoApplication.class, args);
    }

}
