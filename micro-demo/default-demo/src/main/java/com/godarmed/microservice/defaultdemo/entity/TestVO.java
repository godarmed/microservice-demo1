package com.godarmed.microservice.defaultdemo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    @SerializedName(value="remark",alternate={"remark"})
    private String comment;
}
