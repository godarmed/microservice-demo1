package com.godarmed.core.framework.report.service.impl;

import com.godarmed.core.framework.report.service.JdbcTemplateAbstract;

import java.util.List;
import java.util.Map;

public class MysqlTemplateImpl extends JdbcTemplateAbstract {

    /*@Override
    public List<MetaDataBean> syncMetaData(String tableName, String url) {
        //数据库名
        String databaseName = url.substring(url.lastIndexOf("/") + 1);
        //sql
        String sqlStr =
                "SELECT" +
                        "    TABLE_NAME AS tableName," +
                        "    COLUMN_NAME AS columnEngName," +
                        "    COLUMN_COMMENT AS columnChnName," +
                        "    COLUMN_TYPE AS dataType," +
                        "    ORDINAL_POSITION AS columnId," +
                        "    CHARACTER_MAXIMUM_LENGTH AS dataLength," +
                        "    (case when IS_NULLABLE='NO' then '0' else '1' end) AS isNull," +
                        "    (case when COLUMN_KEY='PRI' then '1' else '0' end) AS isPrimaryKey" +
                        " FROM" +
                        "    information_schema.`COLUMNS`" +
                        " WHERE" +
                        "    TABLE_SCHEMA = '" + databaseName + "'" +
                        " AND TABLE_NAME = '" + tableName + "';";

        // 执行元数据查询
        List<MetaDataBean> list = super.jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<>(MetaDataBean.class));

        // 结果判断
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }*/

    @Override
    public List<Map<String, Object>> displayMasterData(String tableName, Integer nowPage, Integer pageSize) {

       /* StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        for (int i = 0; i < mdmHeaderList.size(); i++) {
            if (i == mdmHeaderList.size() - 1) {
                sb.append(mdmHeaderList.get(i).getColumnEngName());
            } else {
                sb.append(mdmHeaderList.get(i).getColumnEngName()).append(",");
            }
        }
        sb.append(" FROM " + tableName + " LIMIT ? , ?");

        // 起始索引
        int startRows = (nowPage - 1) * pageSize;

        List<Map<String, Object>> mapList = super.jdbcTemplate.queryForList(sb.toString(), startRows, pageSize);

        // 结果判断
        if (mapList == null) {
            mapList = new ArrayList<>();
        }

        return mapList;*/
       return null;
    }

    /**
     * 数据导入
     */
    @Override
    public void importMasterData(List<Map<String, Object>> data, String tableName) {


    }

    /**
     * 数据服务
     *
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> serviceMasterData(String tableName) {

        String selectSql = "select * from " + tableName;

        List<Map<String, Object>> mapList = super.jdbcTemplate.queryForList(selectSql);

        return mapList;
    }
}