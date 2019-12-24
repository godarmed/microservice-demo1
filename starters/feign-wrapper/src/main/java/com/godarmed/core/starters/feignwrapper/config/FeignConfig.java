package com.godarmed.core.starters.feignwrapper.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeignConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private String serviceName;
    private String className;
    private Integer connectTimeout = 2000;
    private Integer readTimeout = 180000;
    private int maxAttempts = 1;
    private long period = 100L;
    private long maxPeriod = 1000L;
}
