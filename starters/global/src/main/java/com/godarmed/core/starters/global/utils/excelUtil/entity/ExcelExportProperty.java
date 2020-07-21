package com.godarmed.core.starters.global.utils.excelUtil.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExcelExportProperty extends BaseExportProperty {

	@ApiModelProperty(name = "导出文件的名称前缀")
	private String filePrefix = "test";

	@ApiModelProperty(name = "导出Excel的表头")
	private List<String> heads;

	@ApiModelProperty(name = "单个Excel的最大行数")
	private Integer excelMaxLines = 10000;
}
