package com.godarmed.microservice.defaultdemo.querydslDemo.model.protocol.DTO;

import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Thotel;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * @Classname TcityDTO
 * @Description TODO
 * @Date 2020/4/21 14:38
 * @Created by Administrator
 */
@Data
public class TcityDTO {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String state;

    private String country;

    private String map;

    private Thotel thotel;

    private Integer page;

    private Integer size;
}
