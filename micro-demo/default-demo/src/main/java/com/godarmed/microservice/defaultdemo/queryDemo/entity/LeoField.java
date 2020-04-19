package com.godarmed.microservice.defaultdemo.queryDemo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeoField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fieldName;

    private Object fieldValue;

    private String fieldType;

    private String queryType;

}
