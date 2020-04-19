package com.godarmed.microservice.consumerdemo1.jpa_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.ExcelEntityDetailDTO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.service.ExcelEntityDetailService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excelEntityDetail")
@Api(value = "批量申请记录详情", tags = "批量申请记录详情服务")
@Log4j2
public class ExcelEntityDetailTestController {

    @Autowired
    ExcelEntityDetailService excelEntityDetailService;

    @RequestMapping("/addExcelDetail")
    public ResultModel<ExcelEntityDetail> addExcelDetail(@RequestBody ExcelEntityDetailDTO excelEntityDetailDTO) {
        ResultModel<ExcelEntityDetail> msgReturn = new ResultModel<>();
        ExcelEntityDetail excelEntityDetail = excelEntityDetailService.add(excelEntityDetailDTO);
        msgReturn.setData(excelEntityDetail);
        return msgReturn;
    }


    @RequestMapping("/findExcelDetailById")
    public ResultModel<ExcelEntityDetail> findExcelDetailById(@RequestBody ExcelEntityDetailDTO excelEntityDetailDTO) {
        ResultModel<ExcelEntityDetail> msgReturn = new ResultModel<>();
        ExcelEntityDetail excelEntityDetail = excelEntityDetailService.queryById(excelEntityDetailDTO.getId());
        msgReturn.setData(excelEntityDetail);
        return msgReturn;
    }

    /*@RequestMapping("/findExcelAll")
    public ResultModel<ExcelEntityDetail> findExcelDetailAll(@RequestBody ExcelEntityDetailDTO request) {
        ResultModel<ExcelEntityDetail> msgReturn = new ResultModel<>();
        ExcelEntityDetail excelEntityDetail = excelEntityDetailService.queryByPage(request);
        msgReturn.setData(excelEntityDetail);
        return msgReturn;
    }*/

    @RequestMapping("/deleteExcelDetailById")
    public ResultModel<ExcelEntityDetail> deleteExcelDetailById(@RequestBody ExcelEntityDetailDTO excelEntityDetailDTO) {
        ResultModel<ExcelEntityDetail> msgReturn = new ResultModel<>();
        ExcelEntityDetail excelEntityDetail = excelEntityDetailService.deleteById(excelEntityDetailDTO.getId());
        msgReturn.setData(excelEntityDetail);
        return msgReturn;
    }

}
