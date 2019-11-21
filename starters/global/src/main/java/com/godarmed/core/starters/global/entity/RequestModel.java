package com.godarmed.core.starters.global.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Api("")
@Data
public class RequestModel<T> {
    @ApiModelProperty("每页条数")
    private int count;
    @ApiModelProperty("页数")
    private int page;
    @ApiModelProperty("自定义入参")
    @Valid
    private T request;
}
