package com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;
    private Integer size;
}
