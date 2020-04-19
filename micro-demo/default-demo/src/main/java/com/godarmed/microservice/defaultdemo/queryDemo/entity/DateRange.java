package com.godarmed.microservice.defaultdemo.queryDemo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DateRange implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date startTime;

    private Date endTime;
}
