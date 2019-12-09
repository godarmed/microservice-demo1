package com.godarmed.core.framework.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix="spring.cloud.gateway.open-path")
public class OpenPathProperties {
    private String paths;
}
