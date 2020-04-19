package com.godarmed.microservice.defaultdemo.queryDemo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class IntRange implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer startInt;

    private Integer endInt;
}
