package com.godarmed.microservice.consumerdemo1.common.protocol.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bouncycastle.crypto.tls.ServerName;

import java.io.Serializable;

@Data
public class RequestMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务器名")
    private String serverName;

    @ApiModelProperty("服务器端口号")
    private Integer serverPort;

    @ApiModelProperty("项目名")
    private String contextPath;

    @ApiModelProperty("Servlet路径")
    private String servletPath;

    @ApiModelProperty("参数部分")
    private String queryString;

    @ApiModelProperty(name = "请求URI",notes = "项目名+Servlet路径")
    private String requestURI;



}
