package com.godarmed.microservice.defaultdemo.rocketMQDemo.controller;

import com.godarmed.microservice.defaultdemo.rocketMQDemo.service.RocketMqProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Action;

/**
 * @Classname RocketMqTestController
 * @Description TODO
 * @Date 2020/4/30 17:45
 * @Created by Administrator
 */

@Slf4j
@RestController
public class RocketMqTest {

    @Autowired
    RocketMqProvider rocketMqProvider;

    @GetMapping("/sendMsgTest")
    public void send(String msg) {
        log.info("开始发送消息："+msg);
        boolean sendResult =  rocketMqProvider.send("rockettest:TagA",msg);
        //默认3秒超时
        log.info("消息发送响应信息："+sendResult);
    }
}
