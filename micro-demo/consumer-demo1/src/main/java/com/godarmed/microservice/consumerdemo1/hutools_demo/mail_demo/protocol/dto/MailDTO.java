package com.godarmed.microservice.consumerdemo1.hutools_demo.mail_demo.protocol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "邮件主题")
    private String subject;

    @ApiModelProperty(name = "邮件文字内容")
    private String content;

    /*@ApiModelProperty(name = "邮件附件")
    private String content;*/

    @ApiModelProperty(name = "邮件主题")
    private Boolean isHtml = false;


}
