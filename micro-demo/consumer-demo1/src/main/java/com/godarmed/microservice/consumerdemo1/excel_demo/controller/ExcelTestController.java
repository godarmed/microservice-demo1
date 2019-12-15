package com.godarmed.microservice.consumerdemo1.excel_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.utils.excel.excel.ExcelUtil;
import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.core.starters.redis.lock.annotation.RedisLock;
import com.godarmed.microservice.consumerdemo1.excel_demo.entity.ExportExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.excel_demo.utils.ExcelHelper;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityTaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description
 * @Date 2018-06-05
 * @Time 16:56
 */
@RestController
@RequestMapping("/excelTest")
//@Log
@Log4j2
public class ExcelTestController {

    @Autowired
    ExcelEntityTaskRepository excelEntityTaskRepository;

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "/writeExcel", method = RequestMethod.GET)
    public void writeExcel(HttpServletResponse response) throws IOException {
        Date startTime = new Date();
        log.info("导出Excel开始时间[{}]", startTime);
        List<ExcelEntityTask> list = excelEntityTaskRepository.findAll();
        log.info("共导出条数[{}]条", list.size());
        List<ExportExcelEntityTask> results = list.parallelStream().map(item -> {
            ExportExcelEntityTask result = new ExportExcelEntityTask();
            BeanUtils.copyProperties(item, result);
            return result;
        }).collect(Collectors.toList());
        String fileName = "一个 Excel 文件";
        String sheetName = "第一个 sheet";
        ExcelUtil.writeExcel(response, results, fileName, sheetName, new ExportExcelEntityTask());
        Date endTime = new Date();
        log.info("导出Excel结束时间[{}]，共耗时[{}]ms", endTime, (endTime.getTime() - startTime.getTime()));
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RedisLock(lockName = "USER-" + "leo" + "-Excel")
    @RequestMapping(value = "/writeExcelWithProcess", method = RequestMethod.GET)
    public void writeExcelWithProcess(HttpServletResponse response) {
        Date startTime = new Date();
        log.info("导出Excel开始时间[{}]", startTime);
        List<ExcelEntityTask> list = excelEntityTaskRepository.findAll();
        log.info("共导出条数[{}]条", list.size());
        //结果
        List<List<String>> result;
        //加入标题
        List<List<String>> titles = Arrays.asList(ExcelHelper.readTitleValue(new ExportExcelEntityTask()));
        //加入内容
        List<List<String>> contents = list.parallelStream().map(item -> {
            List<String> content;
            ExportExcelEntityTask temp = new ExportExcelEntityTask();
            BeanUtils.copyProperties(item, temp);
            content = ExcelHelper.readAttributeValue(temp);
            return content;
        }).collect(Collectors.toList());

        result = Stream.concat(titles.stream(),contents.stream()).collect(Collectors.toList());


        String fileName = "一个 Excel 文件";
        String sheetName = "第一个 sheet";

        //ExcelHelper
        ExcelHelper excelHelper = new ExcelHelper(fileName, sheetName, result, "USER-" + "LEO" + "-Excel");
        excelHelper.exportExcel(response);
        Date endTime = new Date();
        log.info("导出Excel结束时间[{}]，共耗时[{}]ms", endTime, (endTime.getTime() - startTime.getTime()));
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "/readProcess", method = RequestMethod.GET)
    public ResultModel<String> readProcess() {
        ResultModel ret = new ResultModel();
        RedisUtils redisUtils = null;
        try {
            redisUtils = new RedisUtils();
            Object process = redisUtils.get("USER-" + "LEO" + "-Excel");
            if (process != null) {
                ret.setData((String) process);
            } else {
                throw new RuntimeException("当前没有正在进行的任务");
            }
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (redisUtils != null) {
                redisUtils.close();
            }
        }
    }


}
