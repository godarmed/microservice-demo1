package com.godarmed.core.framework.logger.consumers;

import com.godarmed.core.framework.logger.core.LogSaveAction;
import com.godarmed.core.framework.logger.exception.LogSaveFailedException;
import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.rabbitmq.entity.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class LogRecordConsumer {
    @Autowired
    LogSaveAction logSaveAction;

    @RabbitListener(queues = "${logger.persistence.queueName:logger.persistence}", concurrency = "${logger.persistence.concurrency:5}")
    public void logRecord(Message<HttpMessage> message) {
        List<HttpMessage> msgs = message.getMessages();
        for (HttpMessage msg : msgs) {
            try {
                logSaveAction.saveLog(msg);
            } catch (LogSaveFailedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

    }
}
