package com.godarmed.core.framework.report.controller;

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

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    @Qualifier("defaultDbcpDataSource")
    BasicDataSource dataSource;

    @RequestMapping("/jdbcTest/getMetaData")
    public Object getMetaDataTest(){
        JdbcTemplateInterface mysqlTemplate = JdbcTemplateFactory.createMdmTemplate("MYSQL", dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        JdbcTemplate myJdbcTemplate  = mysqlTemplate.getJdbcTemplate();
        String sql = "select * from serv_excelentity_detail";
        SqlRowSet result = myJdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metadata = result.getMetaData();
        List<String> metadataList = new ArrayList<String> ();
        System.out.println("====================表结构=============================");
        for(int i = 1; i <= metadata.getColumnCount();i++){
            metadataList.add(metadata.getColumnName(i));
            System.out.print(metadata.getColumnName(i) + "");//name
            System.out.print(metadata.getColumnTypeName(i) + "");//type
            System.out.print(metadata.isCaseSensitive(i) + ""); //null
            System.out.println();//key
        }
        return null;
    }

}
