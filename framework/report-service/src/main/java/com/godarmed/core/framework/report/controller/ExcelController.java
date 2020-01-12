package com.godarmed.core.framework.report.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.AbstractExcelSaxReader;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.godarmed.core.framework.report.constant.GlobalConstants;
import com.godarmed.core.framework.report.model.entity.MetaDataBean;
import com.godarmed.core.framework.report.service.JdbcTemplateFactory;
import com.godarmed.core.framework.report.service.JdbcTemplateInterface;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RequestMapping("/jdbcTest")
@RestController
public class ExcelController {

    @Autowired
    @Qualifier("defaultDbcpDataSource")
    BasicDataSource dataSource;

    @RequestMapping("/getMetaData")
    public Object getMetaDataTest(String tableName) {
        JdbcTemplateInterface mysqlTemplate = JdbcTemplateFactory.createMdmTemplate("MYSQL", dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        JdbcTemplate myJdbcTemplate = mysqlTemplate.getJdbcTemplate();
        final String sql = "SELECT * FROM " + tableName + " WHERE 0 <> 0";
        SqlRowSet result = myJdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metadata = result.getMetaData();
        List<String> metadataList = new ArrayList<String>();
        System.out.println("====================表结构=============================");
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            metadataList.add(metadata.getColumnName(i));
            System.out.println(metadata.getColumnName(i));//name
            System.out.println(metadata.getColumnTypeName(i));//type
            System.out.println(metadata.getCatalogName(i));
            System.out.println(metadata.isCaseSensitive(i)); //null
            System.out.println();//key
        }
        return null;
    }

    @RequestMapping("/importData")
    public Object importData(MultipartFile file, String tableName) {
        List<String> titles = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        AbstractExcelSaxReader reader = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName.contains(".xlsx")) {
                reader = new Excel07SaxReader(createRowHandler(titles, data));
            } else if (fileName.contains(".xls")) {
                reader = new Excel03SaxReader(createRowHandler(titles, data));
            }
            reader.read(file.getInputStream(), -1);
            importData(data, tableName);
            //log.info(JSON.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void importData(List<Map<String, Object>> data, String tableName) {

        //查询表元数据（表头）
        JdbcTemplateInterface mysqlTemplate = JdbcTemplateFactory.createMdmTemplate("MYSQL", dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        List<MetaDataBean> metaDatas = mysqlTemplate.getMetaData(tableName,dataSource.getUrl());

        //校验数据是否合法
        if (!isDataRight(metaDatas, data)) {
            throw new RuntimeException("上传的excel不合法");
        }

        //数据处理,时间转换为毫秒值
        for (Map<String, Object> datum : data) {
            Map<String, Object> temp = null;
            for (String s : datum.keySet()) {
                if (datum.get(s) instanceof DateTime) {
                    long time = ((DateTime) datum.get(s)).getTime();
                    datum.put(s, time);
                }
            }
        }

        mysqlTemplate.importMasterData(metaDatas, data, tableName);
    }

    private Boolean isDataRight(@NotNull List<MetaDataBean> metaDatas, @NotNull List<Map<String, Object>> data) {

        //校验长度是否相同
        if (metaDatas.size() != data.get(0).size()) {
            return false;
        }

        //校验顺序
        Set<String> set = data.get(0).keySet();
        int i = 0;
        for (String s : set) {
            if (!s.equals(metaDatas.get(i).getColumnChnName())) {
                return false;
            }
            i++;
        }
        return true;
    }

    private RowHandler createRowHandler(List<String> titles, List<Map<String, Object>> datas) {
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

    @RequestMapping("/downloadData")
    public Object downloadData(String tableName,String filePath) {
        JdbcTemplateInterface mysqlTemplate = JdbcTemplateFactory.createMdmTemplate(GlobalConstants.DATABASE_TYPE.MYSQL, dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        //获取数据
        List<MetaDataBean> metaDatas = mysqlTemplate.getMetaData(tableName, dataSource.getUrl());
        List<Map<String, Object>> data = mysqlTemplate.displayMasterData(metaDatas, tableName, 1, 100);

        List<List<?>> rows = new ArrayList<>();
        //设置表头
        List<Object> titles = new ArrayList<>();
        for (MetaDataBean metaData : metaDatas) {
            titles.add(metaData.getColumnChnName());
        }
        rows.add(titles);
        //设置内容
        for (Map<String, Object> stringObjectMap : data) {
            List<Object> tempData = new ArrayList<>();
            for (MetaDataBean metaData : metaDatas) {
                tempData.add(stringObjectMap.get(metaData.getColumnEngName()));
            }
            rows.add(tempData);
        }
        //输出文件
        BigExcelWriter writer = null;
        try {
            writer = ExcelUtil.getBigWriter(filePath);
            // 一次性写出内容，使用默认样式
            writer.write(rows);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                // 关闭writer，释放内存
                writer.close();
            }
        }
        return data;
    }

}
