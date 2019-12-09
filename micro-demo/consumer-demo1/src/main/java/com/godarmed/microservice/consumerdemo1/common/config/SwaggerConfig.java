package com.godarmed.microservice.consumerdemo1.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {



    @Value("${swagger.enable}")
    private boolean enableSwagger;


    @Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)//用于配置不同环境的swagger禁止使用
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .groupName("leo微服务测试文档")
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.godarmed.microservice.consumerdemo1"))
                .build();
    }



}
