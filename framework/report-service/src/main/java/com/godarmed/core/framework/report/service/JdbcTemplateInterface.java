package com.godarmed.core.framework.report.service;

import com.godarmed.core.framework.report.model.entity.MetaDataBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface JdbcTemplateInterface {
    /**
     * 同步元数据
     */
    List<MetaDataBean> getMetaData(String tableName, String url);

    /**
     * 数据展现
     */
    List<Map<String, Object>> displayMasterData(List<MetaDataBean> metaData, String tableName, Integer nowPage, Integer pageSize);

    /**
     * 数据导入
     */
    void importMasterData(List<MetaDataBean> metaDatas, List<Map<String, Object>> data, String tableName);

    /**
     * 数据服务
     */
    List<Map<String, Object>> serviceMasterData(String tableName);


    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    JdbcTemplate getJdbcTemplate();

    void setTransactionManager(DataSource dataSource);

    DataSourceTransactionManager getTransactionManager();

    /**
     * 开始事务
     */
    void beginTransaction();

    /**
     * 结束事务
     */
    void endTransaction();
}
