package com.godarmed.microservice.defaultdemo.querydslDemo.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Classname city
 * @Description TODO
 * @Date 2020/4/21 9:56
 * @Created by Administrator
 */
@Data
@Entity
@Table(name = "t_city", schema = "test", catalog = "")
public class Tcity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String state;
    private String country;
    private String map;
}