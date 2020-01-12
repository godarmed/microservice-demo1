package com.godarmed.microservice.consumerdemo1.hutools_demo.excel_demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.godarmed.core.starters.global.entity.ResultModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class HuUtilsExcelTestController {
    @RequestMapping("/HuTool/importExcel")
    public List<List<Object>> importExcel(MultipartFile file){
        //判断excel类型
        String fileName = file.getOriginalFilename();

        ResultModel<List<List<Object>>> resultModel = new ResultModel<>();
        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream(),0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<Object>> data = reader.read();
        return data;
    }
}
