package com.godarmed.springcloud.alibabademo.consumer9600.cloudtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Classname CloudTestController
 * @Description TODO
 * @Date 2020/4/20 17:17
 * @Created by Administrator
 */
@RestController
public class CloudTestController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @GetMapping(value = "/consumer/getServerInfo/{id}")
    public String getServerInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(serverURL+"/cloudTest/"+id, String.class);
    }

}
