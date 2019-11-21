package com.godarmed.core.starters.global.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api(
        value = "统一返回模型",
        tags = {"统一返回模型"}
)
@Data
public class ResultModel<T> {
    @ApiModelProperty("响应信息")
    private String message;
    @ApiModelProperty("响应状态码 0-正常 其他-异常 500X业务异常 403 没有权限 401 token异常")
    private int subCode = 0;
    @ApiModelProperty("响应实体类")
    private T data;
    @ApiModelProperty("分页信息")
    private MsgPageInfo pageInfo;

    public ResultModel() {
        this.message = "";
    }

    public ResultModel(T data) {
        this.data = data;
        this.message = "";
    }

    public ResultModel(String message) {
        this.message = message;
    }

    public ResultModel(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResultModel(int subCode, String message) {
        this.subCode = subCode;
        this.message = message;
    }

    public ResultModel(T data, int subCode, String message) {
        this.data = data;
        this.subCode = subCode;
        this.message = message;
    }

    public void setData(T data, MsgPageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }
}

