package com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page = 0;
    private Integer size = 10;
}
