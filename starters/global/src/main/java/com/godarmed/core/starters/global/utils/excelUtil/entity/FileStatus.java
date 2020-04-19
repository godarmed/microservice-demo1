package com.godarmed.core.starters.global.utils.excelUtil.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class FileStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String path;

    @ApiModelProperty("已完成行数")
    private Integer totalRowNum;

    @ApiModelProperty("已完成行数")
    private AtomicInteger currentRowNum;

    @ApiModelProperty("文件导出进度")
    private AtomicInteger exportProcess;

    @ApiModelProperty("文件导出状态")
    private AtomicBoolean isExport;

    public FileStatus() {
    }

    public FileStatus(Long taskId, String fileName, String path, Integer totalRowNum) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.path = path;
        this.totalRowNum = totalRowNum;
        this.currentRowNum = new AtomicInteger(0);
        this.exportProcess = new AtomicInteger(0);
        this.isExport = new AtomicBoolean(false);
    }
}
