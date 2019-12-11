package com.godarmed.core.starters.rabbitmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(
        prefix = "spring.rabbitmq"
)
@Data
public class RabbitProperties {
    private String addresses;
    private int port = 5672;
    private String username;
    private String password;
    private Integer requestedHeartbeat;
    private String virtualHost;
    private RabbitProperties.Template template = new RabbitProperties.Template();
    private List<Queue> queues = new ArrayList();

    @Data
    public class Template {
        private Long replyTimeout;
    }
}
