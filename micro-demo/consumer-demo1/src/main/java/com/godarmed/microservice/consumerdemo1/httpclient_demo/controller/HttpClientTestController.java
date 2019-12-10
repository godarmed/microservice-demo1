package com.godarmed.microservice.consumerdemo1.httpclient_demo.controller;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.httpclient.HttpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/httpTest")
@Log4j2
@RestController
public class HttpClientTestController {
    @Autowired
    HttpService httpService;

    @RequestMapping("/loginAdmin")
    public ResponseEntity<String> loginAdmin(){
        Map<String,Object> headers=new HashMap<String,Object>();
        Object request = JSON.parseObject("{}");
        ResponseEntity<String> response = null;
        response = httpService.post("http://localhost:9820/login?logoutusername=admin&password=admin", headers, request, String.class);
        log.info(JSON.toJSONString(response));
        return response;
    }

}
