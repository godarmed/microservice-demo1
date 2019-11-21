package com.godarmed.core.starters.global.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class HttpMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String url;
    private String method;
    private String requestHeader;
    private String requestBody;
    private String businessId;
    private String requestId;
    private String status;
    private String exception;
    private String responseHeader;
    private String responseBody;
    private Long elapsedTime;
    private Timestamp responseTime;
}
