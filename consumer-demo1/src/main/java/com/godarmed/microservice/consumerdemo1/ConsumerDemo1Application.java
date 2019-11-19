package com.godarmed.microservice.consumerdemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableCaching
@EnableJpaAuditing
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class ConsumerDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerDemo1Application.class, args);
    }

}
