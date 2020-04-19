package com.godarmed.core.starters.global.utils.excelUtil.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseExportProperties {

    @ApiModelProperty(name = "同时导出任务的数目")
    private Integer coreNumber = 5;

    @ApiModelProperty(name = "同时写文件的线程数")
    private Integer coreWriteNumber = 20;

    @ApiModelProperty(name = "文件存放路径")
    private String storePath= "D:\\images\\temp";

    @ApiModelProperty(name = "文件导出状态传出时间间隔", notes = "单位：毫秒")
    private Integer exportUpdateTime = 500;
    
    @ApiModelProperty(name = "文件下载超时时间", notes = "单位：毫秒")
    private Integer fileMaxOverTime = 86400000;

    @ApiModelProperty(name = "导出文件类型", example = "excel csv txt")
    private String type;

    @ApiModelProperty(name = "导出文件扩展名", example = ".txt .jpg .xlsx")
    private String fileExt;
}
