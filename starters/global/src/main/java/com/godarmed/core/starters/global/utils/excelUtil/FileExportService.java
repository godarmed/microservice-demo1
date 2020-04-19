package com.godarmed.core.starters.global.utils.excelUtil;

import java.util.List;

public interface FileExportService {
    void write(Long taskId, List<String> heads, List<List<Object>> values, String filePrefix, String path);
}
