package com.godarmed.core.framework.logger.scheduletask;

import java.time.LocalDateTime;

import com.godarmed.core.framework.logger.core.LogSaveAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;


@Component
@EnableScheduling   // 2.开启定时任务
@EnableAsync        // 2.开启多线程
@Log4j2
public class LogScheduleTask {
	
	@Autowired
    LogSaveAction logSaveAction;
	
	@Async
    @Scheduled(cron = "0 0 0 * * ?")//3.添加定时任务 每天凌晨0点执行一次0 0 0 * * ? 
    public void configureTasks() {
		log.info("执行删除日志定时任务时间: {}",LocalDateTime.now());
        logSaveAction.delLogTask();
    }
}
