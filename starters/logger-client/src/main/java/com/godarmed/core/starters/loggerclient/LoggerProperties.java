package com.godarmed.core.starters.loggerclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "logger.persistence"
)
@Data
public class LoggerProperties {
    private String queueName = "logger.persistence";
}