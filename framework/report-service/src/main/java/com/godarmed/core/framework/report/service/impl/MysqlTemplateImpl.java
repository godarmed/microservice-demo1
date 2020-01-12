package com.godarmed.core.framework.report.service.impl;

import com.godarmed.core.framework.report.constant.GlobalConstants;
import com.godarmed.core.framework.report.model.entity.MetaDataBean;
import com.godarmed.core.framework.report.service.JdbcTemplateAbstract;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MysqlTemplateImpl extends JdbcTemplateAbstract {

    @Override
    public List<MetaDataBean> getMetaData(String tableName, String url) {
        //数据库名
        String databaseName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));

        //sql
        final String sqlStr =
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
    }

    /**
     * 主数据展现
     *
     * @param metaDatas
     * @param tableName
     * @param nowPage
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> displayMasterData(List<MetaDataBean> metaDatas, String tableName, Integer nowPage, Integer pageSize) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        for (int i = 0; i < metaDatas.size(); i++) {
            sb.append("tb.").append(metaDatas.get(i).getColumnEngName());
            if (i != metaDatas.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(" FROM " + tableName + " AS tb LIMIT ? , ?");

        // 起始索引
        int startRows = (nowPage - 1) * pageSize;

        List<Map<String, Object>> mapList = super.jdbcTemplate.queryForList(sb.toString(), startRows, pageSize);

        // 结果判断
        if (mapList == null) {
            mapList = new ArrayList<>();
        }

        return mapList;
    }

    /**
     * 数据导入
     */
    @Override
    public void importMasterData(List<MetaDataBean> metaDatas, List<Map<String, Object>> data, String tableName) {
        //sql
        final StringBuilder preSql = new StringBuilder();
        final StringBuilder titles = new StringBuilder();
        final StringBuilder values = new StringBuilder();

        //表头及数据
        metaDatas.stream().forEach(item -> {
            titles.append("`").append(item.getColumnEngName()).append("`").append(",");
            values.append(" ? ").append(",");
        });
        titles.deleteCharAt(titles.length() - 1);
        values.deleteCharAt(values.length() - 1);

        //预编译sql
        preSql.append(" insert into ").append(tableName)
                .append(" ( ").append(titles).append(" ) ")
                .append(" values( ").append(values).append(" ) ");

        try {
            //执行语句
            int[] count = jdbcTemplate.batchUpdate(preSql.toString(), new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    //注入参数值
                    try {
                        setvalue(ps, metaDatas, data, i);
                        System.out.println(ps);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int getBatchSize() {
                    //批量执行的数量
                    return data.size();
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
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

    private void setvalue(PreparedStatement ps, List<MetaDataBean> MetaDatas, List<Map<String, Object>> data, int i) throws SQLException {
        for (int j = 0; j < MetaDatas.size(); j++) {
            String dataType = MetaDatas.get(j).getDataType();
            Object obj = data.get(i).get(MetaDatas.get(j).getColumnChnName());
            if (dataType.contains(GlobalConstants.MYSQL_DATA_TYPE.DATE)
                || dataType.contains(GlobalConstants.MYSQL_DATA_TYPE.DATETIME)) {
                ps.setDate(j + 1, new Date((long) obj));
            } else if (dataType.contains(GlobalConstants.MYSQL_DATA_TYPE.TIMESTAMP)) {
                ps.setTimestamp(j + 1, new Timestamp((long) obj));
            } else if (dataType.contains(GlobalConstants.MYSQL_DATA_TYPE.BIGINT)){
                String num = String.valueOf(obj);
                if (!num.trim().equals("")) {
                    if (num.contains(".")) {
                        ps.setDouble(j + 1, Double.valueOf(num));
                    } else {
                        ps.setLong(j + 1, Long.valueOf(num));
                    }
                } else {
                    ps.setString(j + 1, num);
                }
            } else if (dataType.contains(GlobalConstants.MYSQL_DATA_TYPE.INT)) {
                String num = String.valueOf(obj);
                if (!num.trim().equals("")) {
                    if (num.contains(".")) {
                        ps.setDouble(j + 1, Double.valueOf(num));
                    } else {
                        ps.setInt(j + 1, Integer.valueOf(num));
                    }
                } else {
                    ps.setString(j + 1, num);
                }
            }  else  {
                ps.setString(j + 1, (String) obj);
            }
        }
    }
}