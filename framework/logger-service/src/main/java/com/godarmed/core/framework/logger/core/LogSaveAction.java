package com.godarmed.core.framework.logger.core;

import com.godarmed.core.framework.logger.exception.LogSaveFailedException;
import com.godarmed.core.framework.logger.mysql.model.LoggerModel;
import com.godarmed.core.framework.logger.protocol.dto.LogDTO;
import com.godarmed.core.starters.global.entity.HttpMessage;
import org.springframework.data.domain.Page;

public interface LogSaveAction {
	void saveLog(HttpMessage message) throws LogSaveFailedException;
	
	Page<LoggerModel> queryServMenuByPage(LogDTO logDTO);
	
	void delLogTask();
}
