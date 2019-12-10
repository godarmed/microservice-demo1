package com.godarmed.core.starters.httpclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@ConfigurationProperties(
        prefix = "httpcilent"
)
@Data
public class HttpProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxConnect = 1000;
    private int retry = 1;
    private int connectTimeout = 20000;
    private int readTimeout = 300000;
    private int requestWaitTimeout = 20000;
    private int maxPerRouter = 100;
}
