package com.godarmed.core.framework.report.service.impl;

import com.godarmed.core.framework.report.service.JdbcTemplateAbstract;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OracleTemplateImpl extends JdbcTemplateAbstract {

    /**
     * 元数据同步
     *
     * @param tableName
     * @return 数据
     */
    /*@Override
    public List<MetaDataBean> syncMetaData(String tableName, String url) {

        String sqlStr = "SELECT       " +
                "     T1.TABLE_NAME  AS tableName,    " +
                "     T1.COLUMN_NAME AS columnEngName,    " +
                "     T2.COMMENTS    AS columnChnName,       " +
                "     T1.DATA_TYPE   AS dataType,      " +
                "     T1.DATA_LENGTH AS dataLength,    " +
                "     (CASE WHEN T1.NULLABLE='N' then '0' else '1' end) AS isNull,       " +
                "     T1.COLUMN_ID   AS columnId, " +
                " CASE" +
                "  WHEN T1.COLUMN_NAME IN (" +
                "  SELECT COLUMN_NAME FROM user_cons_columns WHERE table_name = '" + tableName + "' AND constraint_name IN" +
                " (" +
                " SELECT " +
                " constraint_name" +
                " FROM " +
                " user_constraints " +
                " WHERE table_name = '" + tableName + "' AND constraint_type = 'P'" +
                " )" +
                " ) " +
                " THEN" +
                "  '1' " +
                "  ELSE '0' " +
                " END AS isPrimaryKey   " +
                " FROM" +
                " USER_TAB_COLUMNS   T1," +
                " USER_COL_COMMENTS  T2 " +
                " WHERE" +
                " T1.COLUMN_NAME =T2.COLUMN_NAME" +
                " AND T1.TABLE_NAME =T2.TABLE_NAME" +
                " AND" +
                " T1.TABLE_NAME=UPPER('" + tableName + "')";

        // 执行元数据查询
        List<MetaDataBean> list = super.jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<>(MetaDataBean.class));

        // 结果判断
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }*/

    /**
     * 主数据展现
     *
     * @param mdmHeaderList
     * @param tableName
     * @param nowPage
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> displayMasterData(String tableName, Integer nowPage, Integer pageSize) {

        /*StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        for (int i = 0; i < mdmHeaderList.size(); i++) {
            if (i == mdmHeaderList.size() - 1) {
                sb.append(mdmHeaderList.get(i).getColumnEngName());
            } else {
                sb.append(mdmHeaderList.get(i).getColumnEngName()).append(",");
            }
        }
        sb.append("  FROM (SELECT T.*, ROWNUM RN " +
                "          FROM (SELECT * " +
                "                  FROM " + tableName +
                "                  ) T " +
                "         WHERE ROWNUM <= ? )  " +
                "  WHERE RN >= ?  ");

        // 起始行数
        int startRows = (nowPage - 1) * pageSize + 1;

        //结束行数
        int endRows = nowPage * pageSize;

        List<Map<String, Object>> mapList = super.jdbcTemplate.queryForList(sb.toString(), endRows, startRows);

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
    @Transactional(rollbackOn = Throwable.class)
    @Override
    public void importMasterData(List<Map<String, Object>> data, String tableName) {

        /*//sql
        final StringBuilder preSql = new StringBuilder();
        final StringBuilder titles = new StringBuilder();
        final StringBuilder values = new StringBuilder();

        //表头及数据
        mtMetaDataEntities.stream().forEach(item -> {
            titles.append(item.getMtEngName()).append(",");
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
                        setvalue(ps, mtMetaDataEntities, data, i);
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
            System.out.println(count);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }*/

    }

    /*private void setvalue(PreparedStatement ps, List<MtMetaDataEntity> MetaDatas, List<Map<String, Object>> data, int i) throws SQLException {
        for (int j = 0; j < MetaDatas.size(); j++) {
            String dataType = MetaDatas.get(j).getMtDataType();
            Object obj = data.get(i).get(MetaDatas.get(j).getMtChnName());
            if (dataType.contains("DATE")) {
                ps.setDate(j + 1, new Date((long) obj));
                //ps.setObject(j+1, new Time((long)obj), Types.DATE);
            } else if (dataType.contains("TIMESTAMP")) {
                ps.setTimestamp(j + 1, new Timestamp((long) obj));
                //ps.setObject(j+1, new Timestamp((long)obj), Types.TIMESTAMP);
            } else if (dataType.contains("NUMBER")) {
                String num = (String)obj;
                if(!num.trim().equals("")){
                    if(num.contains(".")){
                        ps.setDouble(j + 1, Double.valueOf((String) obj));
                    }else{
                        ps.setInt(j + 1, Integer.valueOf((String) obj));
                    }
                }else{
                    ps.setString(j + 1, num);
                }
                //ps.setObject(j+1, Integer.valueOf((String)obj), Types.INTEGER);
            } else if (dataType.contains("VARCHAR2")) {
                ps.setString(j + 1, (String) obj);
                //ps.setObject(j+1, (String)obj, Types.VARCHAR);
            } else {
                ps.setString(j + 1, (String) obj);
            }
        }
    }*/

    /**
     * 数据服务
     */
    @Override
    public List<Map<String, Object>> serviceMasterData(String tableName) {

        String selectSql = "select * from " + tableName;

        List<Map<String, Object>> mapList = super.jdbcTemplate.queryForList(selectSql);

        return mapList;
    }

}
