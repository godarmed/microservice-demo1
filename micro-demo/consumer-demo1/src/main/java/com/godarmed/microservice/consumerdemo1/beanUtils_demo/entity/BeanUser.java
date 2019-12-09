package com.godarmed.microservice.consumerdemo1.beanUtils_demo.entity;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@Data
@Log4j2
public class BeanUser {

    private String name;

    private String address;

    private Integer age;

    private Date birthDate;

    public BeanUser() {
    }

    public String getName() {
        log.info("获取name");
        return name;
    }

    public String getAddress() {
        log.info("获取adress");
        return address;
    }

    public Integer getAge() {
        log.info("获取age");
        return age;
    }

    public Date getBirthDate() {
        log.info("获取birthDate");
        return birthDate;
    }


}
