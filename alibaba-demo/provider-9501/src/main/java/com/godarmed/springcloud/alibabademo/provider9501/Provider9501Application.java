package com.godarmed.springcloud.alibabademo.provider9501;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Provider9501Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9501Application.class, args);
    }

}
