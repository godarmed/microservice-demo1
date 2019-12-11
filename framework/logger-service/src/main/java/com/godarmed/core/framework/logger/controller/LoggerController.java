package com.godarmed.core.framework.logger.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.godarmed.core.framework.logger.core.LogSaveAction;
import com.godarmed.core.framework.logger.mysql.model.LoggerModel;
import com.godarmed.core.framework.logger.protocol.dto.LogDTO;
import com.godarmed.core.framework.logger.protocol.vo.LogVO;
import com.godarmed.core.starters.global.entity.MsgPageInfo;
import com.godarmed.core.starters.global.entity.ResultModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/logger")
@Api(value = "日志记录", tags = "日志记录")
public class LoggerController {
	
	@Autowired
    LogSaveAction logSaveAction;
	
    @ApiOperation(value = "分页查询菜单主页",httpMethod = "POST")
    @PostMapping(value = "/queryLogByPage", consumes="application/json")
    public ResultModel<List<LogVO>> queryServMenuByPage(@RequestBody LogDTO logDTO)  {
    	ResultModel<List<LogVO>> msgReturn = new ResultModel<>();
    	Page<LoggerModel> pageLoggerModel = logSaveAction.queryServMenuByPage(logDTO);
		List<LogVO> listLogVO = pageLoggerModel.stream().map(item->{
			LogVO logVO = new LogVO();
			BeanUtils.copyProperties(item, logVO);
			return logVO;
		}).collect(Collectors.toList());
		msgReturn.setData(listLogVO, MsgPageInfo.loadFromPageable(pageLoggerModel));
        return msgReturn;
    }
}
