package com.godarmed.microservice.consumerdemo1.excel_demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class ExportExcelEntityTask extends BaseRowModel {
    @ExcelProperty(value = "序列号",index = 0)
    private String id;

    @ExcelProperty(value = "任务名",index = 1)
    private String taskName;

    @ExcelProperty(value = "创建时间",index = 2)
    @ApiModelProperty(value = "创建时间(第一次入库)")
    private Date createTime;

    @ExcelProperty(value = "修改时间",index = 3)
    @ApiModelProperty(value = "修改时间(最后一次次入库)")
    private Date updateTime;

    @ExcelProperty(value = "操作人名称",index = 4)
    @ApiModelProperty(value = "操作人名称")
    private String createName;

    @ExcelProperty(value = "任务量",index = 5)
    @ApiModelProperty(value = "任务量")
    private Integer total;
}
