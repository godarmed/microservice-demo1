package com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.vo;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryExcelEntityDetailVO {
    @ApiModelProperty(value = "任务Id")
    private Long id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "完成百分比（0-100）")
    Integer applyProcess;

    @ApiModelProperty(value = "成功条数")
    Integer successNum;

    @ApiModelProperty(value = "失败条数")
    Integer failureNum;

    @ApiModelProperty(value = "总条数")
    private Integer total;

    @ApiModelProperty(value = "创建时间(第一次入库)")
    private Date createTime;

    @ApiModelProperty(value = "操作人名称")
    private String createName;

    @ApiModelProperty(value = "任务详情")
    List<ExcelEntityDetail> excelEntityDetails;

}
