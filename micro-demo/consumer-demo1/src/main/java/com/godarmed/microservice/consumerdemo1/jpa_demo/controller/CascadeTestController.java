package com.godarmed.microservice.consumerdemo1.jpa_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.utils.SpringUtils;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityDetailRepository;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityTaskRepository;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cascadeTest")
@Api(value = "级联关系测试", tags = "级联关系测试")
@Log4j2
public class CascadeTestController {

    @Autowired
    ExcelEntityTaskRepository excelEntityTaskRepository;

    @Autowired
    ExcelEntityDetailRepository excelEntityDetailRepository;

    @RequestMapping("/createExcelTask")
    public ResultModel<ExcelEntityTask> createExcelTask() {
        ResultModel<ExcelEntityTask> msgReturn = new ResultModel<>();
        ExcelEntityTask excelEntityTask = new ExcelEntityTask();
        excelEntityTask.setTaskName("test-one");
        excelEntityTask.setCreateName("leo");

        List<ExcelEntityDetail> excelEntityDetails = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ExcelEntityDetail excelEntityDetail = new ExcelEntityDetail();
            excelEntityDetail.setMerchantName("leo's company");
            excelEntityDetail.setExcelEntityTask(excelEntityTask);
            excelEntityDetails.add(excelEntityDetail);
        }

        if(excelEntityTask.getExcelEntityDetailList() != null){
            excelEntityTask.getExcelEntityDetailList().clear();
            excelEntityTask.getExcelEntityDetailList().addAll(excelEntityDetails);
        } else {
            excelEntityTask.setExcelEntityDetailList(excelEntityDetails);
        }

        excelEntityTaskRepository.save(excelEntityTask);
        msgReturn.setData(excelEntityTask);
        return msgReturn;
    }

    @RequestMapping("/delExcelTask")
    public void delExcelTask(String id) {
        if(excelEntityTaskRepository.existsById(id)){
            excelEntityTaskRepository.deleteById(id);
        }
    }

    @RequestMapping("/swapExcelDetails")
    public void swapExcelDetails(String oldTaskId,String newTaskId) {
        ExcelEntityTask oldTask = excelEntityTaskRepository.findById(oldTaskId).get();
        ExcelEntityTask newTask = excelEntityTaskRepository.findById(newTaskId).get();

        List<ExcelEntityDetail> oldExcelDetails = oldTask.getExcelEntityDetailList();
        List<ExcelEntityDetail> newExcelDetails = newTask.getExcelEntityDetailList();

        if(oldExcelDetails!=null){
            for (ExcelEntityDetail oldExcelDetail : oldExcelDetails) {
                oldExcelDetail.setExcelEntityTask(newTask);
            }
        }

        if(newExcelDetails!=null){
            for (ExcelEntityDetail newExcelDetail : newExcelDetails) {
                newExcelDetail.setExcelEntityTask(oldTask);
            }
        }

        excelEntityDetailRepository.saveAll(oldExcelDetails);
        excelEntityDetailRepository.saveAll(newExcelDetails);

    }



    @RequestMapping("/delExcelDetail")
    public void delExcelDetail(String id) {
        ExcelEntityTask excelEntityTask = null;
        if((excelEntityTask = excelEntityTaskRepository.findById(id).get()) != null){
            excelEntityTask.getExcelEntityDetailList().clear();
            excelEntityTaskRepository.save(excelEntityTask);
        }
    }


}
