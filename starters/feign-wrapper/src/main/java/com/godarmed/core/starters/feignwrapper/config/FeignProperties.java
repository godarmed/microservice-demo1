package com.godarmed.core.starters.feignwrapper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(
        prefix = "feign.wrapper"
)
public class FeignProperties implements Serializable {
    public static final String defaultClient = "default";
    private static final long serialVersionUID = 1L;
    private String protocol = "http";
    private String gateway = "Gateway";
    private String headerClass = "com.eseasky.core.starters.feign.wrapper.config.DefaultHeaderSet";
    private List<FeignConfig> clients = new ArrayList();
}
