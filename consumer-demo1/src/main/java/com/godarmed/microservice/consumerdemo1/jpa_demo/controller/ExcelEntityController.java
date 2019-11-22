package com.godarmed.microservice.consumerdemo1.jpa_demo.controller;

import com.godarmed.core.starters.global.entity.MsgPageInfo;
import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.BaseExcelEntityDTO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.vo.QueryExcelEntityDetailVO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.service.ExcelEntityService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excelEntity")
@Api(value = "批量申请记录查询", tags = "批量申请记录服务")
@Log4j2
public class ExcelEntityController {
    @Autowired
    ExcelEntityService excelEntityService;

    @RequestMapping("/createExcelTask")
    public ResultModel<ExcelEntityTask> createExcelTask(@RequestBody BaseExcelEntityDTO baseExcelEntityDTO) {
        ResultModel<ExcelEntityTask> msgReturn = new ResultModel<>();
        ExcelEntityTask entityTask = new ExcelEntityTask();
        BeanUtils.copyProperties(baseExcelEntityDTO,entityTask);
        entityTask = excelEntityService.saveExcelEntityTask(entityTask);
        msgReturn.setData(entityTask);
        return msgReturn;
    }

    @RequestMapping("/createExcelDetail")
    public ResultModel<ExcelEntityDetail> createExcelDetail(@RequestBody BaseExcelEntityDTO baseExcelEntityDTO) {
        ResultModel<ExcelEntityDetail> msgReturn = new ResultModel<>();
        ExcelEntityDetail entityDetail = new ExcelEntityDetail();
        BeanUtils.copyProperties(baseExcelEntityDTO,entityDetail);
        entityDetail = excelEntityService.saveExcelEntityDetail(entityDetail);
        msgReturn.setData(entityDetail);
        return msgReturn;
    }

    //查询
    @RequestMapping("/queryExcelTask")
    public ResultModel<List<QueryExcelEntityDetailVO>> query(@RequestBody BaseExcelEntityDTO baseExcelEntityDTO) {
        ResultModel<List<QueryExcelEntityDetailVO>> msgReturn = new ResultModel<>();
        List<QueryExcelEntityDetailVO> queryExcelEntityDetailVOS = new ArrayList<>();
        //查询所有任务
        Page<ExcelEntityTask> listTask = excelEntityService.queryExcelEntityTask(baseExcelEntityDTO);
        for (ExcelEntityTask task : listTask) {
            QueryExcelEntityDetailVO queryExcelEntityDetailVO = new QueryExcelEntityDetailVO();
            BeanUtils.copyProperties(task,queryExcelEntityDetailVO);
            queryExcelEntityDetailVO.setExcelEntityDetails(queryExcelEntityDetail(task,queryExcelEntityDetailVO));
            queryExcelEntityDetailVO.setApplyProcess(100 * queryExcelEntityDetailVO.getExcelEntityDetails().size() / task.getTotal());
            queryExcelEntityDetailVOS.add(queryExcelEntityDetailVO);
        }
        msgReturn.setData(queryExcelEntityDetailVOS, MsgPageInfo.loadFromPageable(listTask));
        return msgReturn;
    }

    //查询单个任务下的所有
    public List<ExcelEntityDetail> queryExcelEntityDetail(ExcelEntityTask excelEntityTask, QueryExcelEntityDetailVO excelEntityDetailVO) {

        //成功条数
        AtomicInteger successNum = new AtomicInteger(0);
        //失败条数
        AtomicInteger failureNum = new AtomicInteger(0);

        List<ExcelEntityDetail> pageBatchPorts = excelEntityTask.getExcelEntityDetailList();
        pageBatchPorts = pageBatchPorts.stream()
                .map(item -> {     //计数并转换对象
                    if (item.getStatus().equals("失败")) {
                        failureNum.addAndGet(1);
                    } else {
                        successNum.addAndGet(1);
                    }
                    return item;
                }).sorted(  //按行号排序
                    Comparator.comparing(ExcelEntityDetail::getRows))
                .collect(Collectors.toList());
        excelEntityDetailVO.setSuccessNum(successNum.get());
        excelEntityDetailVO.setFailureNum(failureNum.get());

        return pageBatchPorts;
    }

    //删除
    @RequestMapping("/deleteExcelTask")
    public ResultModel<String> deleteExcelEntityTask(@RequestBody BaseExcelEntityDTO baseDTO) {
        //校验是否可以删除
        ExcelEntityTask excelEntityTask;
        try{
            excelEntityTask = excelEntityService.queryExcelEntityTaskById(baseDTO.getId());
        }catch (Exception e){
            throw new RuntimeException("该任务不存在");
        }
        List<ExcelEntityDetail> pageBatchPorts = excelEntityTask.getExcelEntityDetailList();
        if(pageBatchPorts==null || pageBatchPorts.size() < excelEntityTask.getTotal()){
            throw new RuntimeException("该任务未执行完,无法删除");
        }
        ResultModel<String> msgReturn = new ResultModel<>();
        try {
            msgReturn.setData(excelEntityService.deleteExcelEntityTask(baseDTO.getId()));
        }catch (Exception e){
            msgReturn.setSubCode(500);
            msgReturn.setMessage("删除失败");
        }
        //删除详情
        return msgReturn;
    }

    //删除
    @RequestMapping("/deleteExcelDetail")
    public ResultModel<Long> deleteExcelEntityTaskTest(@RequestBody BaseExcelEntityDTO baseDTO) {
        ResultModel<Long> msgReturn = new ResultModel<>();
        try {
            msgReturn.setData(excelEntityService.deleteExcelEntityDetail(Long.valueOf(baseDTO.getId())));
        }catch (Exception e){
            msgReturn.setSubCode(500);
            msgReturn.setMessage("删除失败");
        }
        //删除详情
        return msgReturn;
    }

}
