package com.godarmed.microservice.consumerdemo1.common.controller;

import com.eseasky.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.common.protocol.vo.RequestMsg;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/commonTest")
public class CommonTestController {
    @RequestMapping("/test")
    public ResultModel<RequestMsg> testOne(HttpServletRequest req) {
        ResultModel<RequestMsg> resultModel = new ResultModel<>();
        RequestMsg requestMsg = new RequestMsg();
        BeanUtils.copyProperties(req, requestMsg);
        requestMsg.setQueryString(req.getQueryString());
        resultModel.setData(requestMsg);
        return resultModel;
    }
}
