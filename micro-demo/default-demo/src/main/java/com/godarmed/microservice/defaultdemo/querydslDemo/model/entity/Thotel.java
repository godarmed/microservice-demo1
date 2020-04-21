package com.godarmed.microservice.defaultdemo.querydslDemo.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Classname Thotel
 * @Description TODO
 * @Date 2020/4/21 9:57
 * @Created by Administrator
 */
@Data
@Entity
@Table(name = "t_hotel", schema = "test", catalog = "")
public class Thotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private Integer city;//保存着城市的id主键
}