package com.godarmed.microservice.consumerdemo1.hutools_demo.excel_demo;


import cn.hutool.poi.excel.sax.AbstractExcelSaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HuToolExcelTestController {

    public static void main(String[] args) {
        File file = new File("D:\\Documents\\Tencent Files\\2570613257\\FileRecv\\huoyun_货运_20200106162113.xlsx");
        System.out.println(file.exists());
        List<String> titles = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        AbstractExcelSaxReader reader = null;
        try {

            reader = new Excel07SaxReader(createRowHandler(titles, data));
            reader.read(file, -1);

        } finally {
            System.out.println(JSON.toJSONString(data));
        }

    }

    private static RowHandler createRowHandler(List<String> titles, List<Map<String, Object>> datas) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
                if (rowIndex == 0) {
                    for (Object o : rowList) {
                        titles.add((String) o);
                    }
                } else {
                    if (!isEmptyRow(rowList)) {
                        Map<String, Object> data = new LinkedHashMap<>();
                        for (int i = 0; i < titles.size(); i++) {
                            data.put(titles.get(i), rowList.get(i));
                        }
                        datas.add(data);
                    }
                }
                //Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
            }

            private Boolean isEmptyRow(List<Object> rowList) {
                for (Object o : rowList) {
                    if (o != null && !o.toString().trim().equals("")) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}
