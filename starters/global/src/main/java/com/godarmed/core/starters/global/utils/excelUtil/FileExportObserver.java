package com.godarmed.core.starters.global.utils.excelUtil;

import com.godarmed.core.starters.global.utils.excelUtil.entity.FileStatus;

import java.util.List;

public interface FileExportObserver {
	public void update(List<FileStatus> fileStatusList);
}
