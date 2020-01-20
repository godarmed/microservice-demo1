package com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExcelEntityDetailDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer rows;

    private Long merchantId;

    private String merchantName;

    private String port;

    private String sign;

    private String usage;

    private String province;

    private String authorizationFiles;

    private Date validUntil;

    private Date validStart;

    private String status;

    private String message;

    @ApiModelProperty(value = "创建时间(第一次入库)")
    private Date createTime;

    @ApiModelProperty(value = "操作时间(变更时间)")
    private Date operTime;

    @ApiModelProperty(value = "操作人名称")
    private String operateName;

    @ApiModelProperty(value = "错误码")
    private Integer errorcode = 0;


}
