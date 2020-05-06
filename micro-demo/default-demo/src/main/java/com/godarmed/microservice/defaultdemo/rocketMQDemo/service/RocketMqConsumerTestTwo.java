package com.godarmed.microservice.defaultdemo.rocketMQDemo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2020/5/6 15:42
 * @Created by Administrator
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "rockettest",selectorExpression = "TagB", consumerGroup = "myconsumer2")
public class RocketMqConsumerTestTwo implements RocketMQReplyListener<String,String>, RocketMQPushConsumerLifecycleListener {

    @Override
    public String onMessage(String message) {
        log.info("消费者{},收到消息：{}", "two" ,message);
        return message;
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // set consumer consume message from now
        //consumer.setMessageModel(MessageModel.BROADCASTING);
        log.info("消费者配置为:{}",consumer);
    }

}
