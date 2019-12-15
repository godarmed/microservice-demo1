package com.godarmed.microservice.consumerdemo1.validater_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.loggerclient.annotation.RequestStart;
import com.godarmed.microservice.consumerdemo1.validater_demo.protocol.dto.PersonDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/validTest")
public class ValidaterTestController {

    @RequestStart
    @RequestMapping("/add")
    public ResultModel add(@RequestBody @Validated({PersonDTO.AddPersonGroup.class}) PersonDTO personDTO) {
        ResultModel resultModel = new ResultModel();
        resultModel.setData(personDTO);
        return resultModel;
    }

    @RequestMapping("/update")
    public ResultModel update(@RequestBody @Validated({PersonDTO.UpdatePersonGroup.class}) PersonDTO personDTO) {
        ResultModel resultModel = new ResultModel();
        resultModel.setData(personDTO);
        return resultModel;
    }

    @RequestMapping("/query")
    public ResultModel query(@RequestBody @Validated PersonDTO personDTO) {
        ResultModel resultModel = new ResultModel();
        resultModel.setData(personDTO);
        return resultModel;
    }

    @RequestMapping("/delete")
    public ResultModel delete(@RequestBody @Validated({PersonDTO.DeletePersonGroup.class}) PersonDTO personDTO) {
        ResultModel resultModel = new ResultModel();
        resultModel.setData(personDTO);
        return resultModel;
    }

}
