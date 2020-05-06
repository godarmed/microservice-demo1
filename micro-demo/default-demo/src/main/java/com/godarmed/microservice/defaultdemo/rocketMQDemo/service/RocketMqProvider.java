package com.godarmed.microservice.defaultdemo.rocketMQDemo.service;

import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname RocketMqProvider
 * @Description TODO
 * @Date 2020/4/30 17:42
 * @Created by Administrator
 */
@Service
public class RocketMqProvider {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public boolean send(String topic, String msg){
        rocketMQTemplate.convertAndSend(topic, msg);
        return true;
    }
}
