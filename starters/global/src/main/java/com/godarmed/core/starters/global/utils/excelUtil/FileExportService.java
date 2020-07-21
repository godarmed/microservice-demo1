package com.godarmed.core.starters.global.utils.excelUtil;

import com.godarmed.core.starters.global.utils.excelUtil.impl.ExcelFileWriter;
import com.godarmed.core.starters.global.utils.excelUtil.impl.TxtWriter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public interface FileExportService {
    void write(Long taskId, List<String> heads, List<List<Object>> values, String filePrefix, String path);

    /**
     * 获取对应文件扩展名
     * @param type
     * @return
     */
    default String getFileExt(String type) {
        switch (type) {
            case "excel":
                return ".xlsx";
            case "txt":
                return ".txt";
            case "csv":
                return ".csv";
            default:
                return ".xlsx";
        }
    }

    /**
     * 获取对应文件写出对象
     * @param type
     * @return
     */
    default IFileWriter getWriter(String type) {
        switch (type) {
            case "excel":
                return new ExcelFileWriter();
            case "txt":
                return new TxtWriter();
            case "csv":
                return new ExcelFileWriter();
            default:
                return new TxtWriter();
        }
    }

    /**
     * 获取线程池对象
     * @return
     */
    default ThreadPoolTaskExecutor taskExecutor(int coreWriteNumber) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        if(coreWriteNumber>50){
            executor.setCorePoolSize(50);
        }else{
            executor.setCorePoolSize(coreWriteNumber);
        }
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("taskExecutor-file-export-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
