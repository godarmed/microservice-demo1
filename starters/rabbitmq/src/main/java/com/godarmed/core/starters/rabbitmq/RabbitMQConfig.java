package com.godarmed.core.starters.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties({RabbitProperties.class})
public class RabbitMQConfig {
    @Autowired
    RabbitProperties rabbitProperties;

    public RabbitMQConfig() {
    }

    @Bean(
            name = {"connectionFactory"}
    )
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(this.rabbitProperties.getUsername());
        connectionFactory.setPassword(this.rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(this.rabbitProperties.getVirtualHost());
        connectionFactory.setPort(this.rabbitProperties.getPort());
        if (this.rabbitProperties.getRequestedHeartbeat() != null) {
            connectionFactory.setRequestedHeartBeat(this.rabbitProperties.getRequestedHeartbeat());
        }

        connectionFactory.setPublisherConfirms(true);
        ExecutorService service = Executors.newFixedThreadPool(500);
        connectionFactory.setExecutor(service);
        connectionFactory.setConnectionTimeout(15000);
        connectionFactory.setRequestedHeartBeat(60);
        connectionFactory.setAddresses(this.rabbitProperties.getAddresses());
        return connectionFactory;
    }

    @Bean
    public String[] mqMsgQueues() throws AmqpException, IOException {
        List<Queue> queues = this.rabbitProperties.getQueues();
        if (queues == null) {
            return null;
        } else {
            String[] queueNames = new String[queues.size()];
            Channel channel = this.connectionFactory().createConnection().createChannel(false);

            for (int i = 0; i < queues.size(); ++i) {
                Queue item = (Queue) queues.get(i);
                channel.queueDeclare(item.getName(), true, false, false, (Map) null);
                channel.exchangeDeclare(item.getExchangeName(), item.getType(), true);
                channel.queueBind(item.getName(), item.getExchangeName(), item.getBindName());
                queueNames[i] = item.getName();
            }

            return queueNames;
        }
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("rpc.exchange");
    }
}

