package com.godarmed.microservice.consumerdemo1.validater_demo.controller;

import com.godarmed.microservice.consumerdemo1.validater_demo.config.NumInValidator;
import com.godarmed.microservice.consumerdemo1.validater_demo.protocol.dto.ResourceBindingRuleDTO;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/resourceBindRule")
@Api(value = "资源绑定规则管理接口", tags = "资源绑定规则管理服务")
@Log4j2
public class ResourceBindRuleController {

    @RequestMapping(value = "/addBindRuleTest")
    public Object add(@RequestBody @Validated(ResourceBindingRuleDTO.AddOne.class) ResourceBindingRuleDTO request){
        return request;
    }

    @RequestMapping(value = "/updateBindRuleTest")
    public Object update(@RequestBody @Validated(ResourceBindingRuleDTO.UpdateOne.class) ResourceBindingRuleDTO request){
        return request;
    }

    @RequestMapping(value = "/queryBindRulesTest")
    public Object query(@RequestBody @NotNull ResourceBindingRuleDTO request) {
        return request;
    }

    @RequestMapping(value = "/delBindRuleTest")
    public Object delete(@RequestBody @Validated(ResourceBindingRuleDTO.DeleteOne.class) ResourceBindingRuleDTO request){
        return request;
    }

    @RequestMapping(value = "/resetNumList")
    public void resetNumList(String numsName,int[] nums){
        NumInValidator.resetNumArray(numsName, Arrays.stream(nums).boxed().collect(Collectors.toList()));
    }


}
