package com.godarmed.core.framework.authservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.godarmed.core.framework.authservice.UserTools.UserToolsService;
import com.godarmed.core.framework.authservice.UserTools.entity.DatabaseEntity;
import com.godarmed.core.framework.authservice.UserTools.entity.ExecuteSQLEntity;
import com.godarmed.core.starters.global.entity.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "用户工具", tags = "用户工具服务")
@RestController
@Log4j2
@RequestMapping("/UserTools")
public class UserToolsController {
   /* @Autowired
    private UserToolsService userToolsService;

    @ApiOperation(value = "执行sql", httpMethod = "POST")
    @PostMapping(value = "/executeSQL")
    public ResultModel<List<List<Object>>> executeSQL(@RequestBody ExecuteSQLEntity executeSQL) {
        ResultModel<List<List<Object>>> msgReturn = new ResultModel<>();
        List<List<Object>> resList = null;
		try {
			resList = userToolsService.executeSQL(executeSQL);
	        log.info(JSONObject.toJSONString(resList));
	        msgReturn.setData(resList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgReturn.setSubCode(500);
			msgReturn.setMessage(e.getMessage());
		}
        return msgReturn;
    }
    
    @ApiOperation(value = "备份数据库", httpMethod = "POST")
    @PostMapping(value = "/backupDatabase")
    public ResultModel<String> backupDatabase(@RequestBody DatabaseEntity databaseEntity) {
    	ResultModel<String> msgReturn = new ResultModel<>();
        String result = null;
        try {
        	result = userToolsService.backupDatabase(databaseEntity);
            log.info(JSONObject.toJSONString(result));
            msgReturn.setData(result);
        } catch (JSchException e) {
            msgReturn.setSubCode(500);
            msgReturn.setMessage("用户名密码错误!");
        }catch (CommandException e){
            msgReturn.setSubCode(500);
            msgReturn.setMessage("command命令不合法!");
        }
        catch (Exception e){
            msgReturn.setSubCode(500);
            msgReturn.setMessage(e.getMessage());
        }
        return msgReturn;
    }
    
    @ApiOperation(value = "恢复数据库", httpMethod = "POST")
    @PostMapping(value = "/restoreDatabase")
    public ResultModel<String> restoreDatabase(@RequestBody DatabaseEntity databaseEntity) {
        ResultModel<String> msgReturn = new ResultModel<>();
        String result = null;
		try {
			result = userToolsService.restoreDatabase(databaseEntity);
	        log.info(JSONObject.toJSONString(result));
	        msgReturn.setData(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgReturn.setSubCode(500);
			msgReturn.setMessage(e.getMessage());
		}
        return msgReturn;
    }*/
}
