package com.godarmed.springcloud.alibabademo.consumer9600.cloudtest.config;

import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname CommonConfig
 * @Description TODO
 * @Date 2020/4/20 17:30
 * @Created by Administrator
 */
@Configuration
public class CommonConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
