package com.godarmed.core.framework.authservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.godarmed.core.framework.authservice.module.model.ServUserInfo;
import com.godarmed.core.framework.authservice.module.service.ServUserInfoService;
import com.godarmed.core.framework.authservice.protocol.dto.ServUserInfoDTO;
import com.godarmed.core.framework.authservice.protocol.vo.ServUserInfoVO;
import com.godarmed.core.starters.global.entity.MsgPageInfo;
import com.godarmed.core.starters.global.entity.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "用户管理", tags = "用户管理服务")
@RestController
@Log4j2
@RequestMapping("/UserInfoManage")
public class UserInfoController {
    @Autowired
    private ServUserInfoService servUserInfoService;

    @ApiOperation(value = "新建用户", httpMethod = "POST")
    @PostMapping(value = "/add")
    public ResultModel<ServUserInfoVO> addUserInfo(@RequestBody ServUserInfoDTO servUserInfoDTO) {

        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO servUserInfoVO = servUserInfoService.addUserInfo(servUserInfoDTO);
        log.info(JSONObject.toJSONString(servUserInfoVO));
        msgReturn.setData(servUserInfoVO);
        return msgReturn;
    }

    @ApiOperation(value = "查询用户", httpMethod = "POST")
    @PostMapping(value = "/query")
    public ResultModel<List<ServUserInfoVO>> queryUserInfo(@RequestBody ServUserInfoDTO servUserInfoDTO) {

        ResultModel<List<ServUserInfoVO>> msgReturn = new ResultModel<>();
        Page<ServUserInfo> page = servUserInfoService.queryUserInfo(servUserInfoDTO);
        List<ServUserInfoVO> list = page.stream().map(item -> {
            ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
            BeanUtils.copyProperties(item, servUserInfoVO);
            return servUserInfoVO;
        }).collect(Collectors.toList());

        log.info(JSONObject.toJSONString(list));
        msgReturn.setData(list, MsgPageInfo.loadFromPageable(page));

        return msgReturn;
    }


    @ApiOperation(value = "修改用户", httpMethod = "POST")
    @PostMapping(value = "/updateUserInfo", consumes = "application/json")
    public ResultModel<ServUserInfoVO> updateUserInfo(@RequestBody ServUserInfoDTO servUserInfoDTO) {
        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO servUserInfoVO = servUserInfoService.updateServUserInfo(servUserInfoDTO);
        msgReturn.setData(servUserInfoVO);
        return msgReturn;
    }

    @ApiOperation(value = "根据id删除用户", httpMethod = "POST")
    @PostMapping(value = "/delUserInfoById", consumes = "application/json")
    public ResultModel<ServUserInfoVO> delUserInfoById(@RequestBody ServUserInfoDTO servUserInfoDTO) {
        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO servUserInfoVO = servUserInfoService.deleteServUserInfoById(servUserInfoDTO);
        msgReturn.setData(servUserInfoVO);
        return msgReturn;
    }

    @ApiOperation(value = "更换密码", httpMethod = "POST")
    @PostMapping(value = "/changepwd")
    public ResultModel<ServUserInfoVO> changePwd(@RequestBody ServUserInfoDTO servUserInfoDTO) {
        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO userInfoVO = servUserInfoService.updatePwd(servUserInfoDTO);

        msgReturn.setData(userInfoVO);
        return msgReturn;
    }

    @ApiOperation(value = "查看用户", httpMethod = "POST")
    @PostMapping(value = "/getuserinfo")
    public ResultModel<ServUserInfoVO> getUserInfo(@RequestBody ServUserInfoDTO servUserInfoDTO) {
        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO servUserInfoVO = servUserInfoService.findById(servUserInfoDTO);
        msgReturn.setData(servUserInfoVO);
        return msgReturn;
    }

    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "强制下线", httpMethod = "POST")
    @PostMapping(value = "/forceOffLine")
    public ResultModel<ServUserInfoVO> forceOffLine(@RequestBody ServUserInfoDTO servUserInfoDTO) {
        ResultModel<ServUserInfoVO> msgReturn = new ResultModel<>();
        ServUserInfoVO servUserInfoVO = servUserInfoService.forceOffLine(servUserInfoDTO);
        msgReturn.setData(servUserInfoVO);
        return msgReturn;
    }

}
