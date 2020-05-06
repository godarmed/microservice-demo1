package com.godarmed.microservice.defaultdemo.querydslDemo.model.protocol.VO;

import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Thotel;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @Classname TcityVO
 * @Description TODO
 * @Date 2020/4/21 14:32
 * @Created by Administrator
 */
@Data
public class TcityVO implements Serializable {

        private static final long serialVersionUID = 1L;

        private int id;

        private String name;

        private String state;

        private String country;

        private String map;

        private List<Thotel> thotels;
}
