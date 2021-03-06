package com.godarmed.springcloud.alibabademo.provider9500.cloudtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope   //支持Nacos的动态刷新
public class CloudTestController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping(value = "/cloudTest/{id}")
    public String getServerInfo(@PathVariable("id") Integer id){
        return "nacos registry, serverPort: " + serverPort + "\t id " + id;
    }

    @GetMapping("/config/info")
    public String getConfigInfo(){
        return configInfo;
    }
}
