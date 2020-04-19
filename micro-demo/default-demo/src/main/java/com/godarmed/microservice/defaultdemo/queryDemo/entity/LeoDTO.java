package com.godarmed.microservice.defaultdemo.queryDemo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LeoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    List<LeoField> leoFields;
}
