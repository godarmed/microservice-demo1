package com.godarmed.microservice.defaultdemo.queryDemo.controller;

import com.godarmed.microservice.defaultdemo.queryDemo.entity.LeoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class QueryTestController {
    @RequestMapping("/queryTest")
    public LeoDTO queryTest(@RequestBody LeoDTO request) {
        log.info(request);
        return request;
    }
}
