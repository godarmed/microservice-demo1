package com.godarmed.springcloud.alibabademo.provider9501.cloudtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname CloudTestController
 * @Description TODO
 * @Date 2020/4/20 16:30
 * @Created by Administrator
 */
@RestController
public class CloudTestController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/cloudTest/{id}")
    public String getServerInfo(@PathVariable("id") Integer id){
        return "nacos registry, serverPort: " + serverPort + "\t id " + id;
    }
}
