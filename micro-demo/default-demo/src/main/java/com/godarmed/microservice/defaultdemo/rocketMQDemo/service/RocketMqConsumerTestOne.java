package com.godarmed.microservice.defaultdemo.rocketMQDemo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2020/4/30 17:43
 * @Created by Administrator
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "rockettest",selectorExpression = "TagA", consumerGroup = "myconsumer1")
public class RocketMqConsumerTestOne implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener{    @Override

    public void onMessage(String message) {
        log.info("消费者{},收到消息：{}", "one" ,message);
        if(message.contains("FFF")){
            System.out.println(1/0);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // set consumer consume message from now
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setMaxReconsumeTimes(2);
        log.info("消费者配置为:{}",consumer);
    }
}
