package com.godarmed.core.starters.loggerclient.service.impl;

import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.loggerclient.LoggerProperties;
import com.godarmed.core.starters.loggerclient.service.LoggerService;
import com.godarmed.core.starters.rabbitmq.entity.Message;
import com.godarmed.core.starters.rabbitmq.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MqLoggerServiceImpl implements LoggerService{
    @Autowired
    MqService mqService;
    @Autowired
    LoggerProperties loggerProperties;

    public MqLoggerServiceImpl() {
    }

    @Override
    public void logger(HttpMessage message) {
        List<HttpMessage> msges = new ArrayList();
        msges.add(message);
        Message<HttpMessage> queueMessage = new Message();
        queueMessage.setCreateTime(Timestamp.from((new Date()).toInstant()));
        queueMessage.setQueueName(this.loggerProperties.getQueueName());
        queueMessage.setMessages(msges);
        this.mqService.push(queueMessage);
    }
}
