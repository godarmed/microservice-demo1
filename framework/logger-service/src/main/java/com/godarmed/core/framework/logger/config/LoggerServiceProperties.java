package com.godarmed.core.framework.logger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="logger.persistence")
public class LoggerServiceProperties {
	private String queueName = "logger.persistence";
	
	private Integer concurrency = 5;
	
	
}
