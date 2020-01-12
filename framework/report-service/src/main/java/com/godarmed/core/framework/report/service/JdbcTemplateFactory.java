package com.godarmed.core.framework.report.service;

import com.godarmed.core.framework.report.constant.GlobalConstants;
import com.godarmed.core.framework.report.service.impl.MysqlTemplateImpl;
import com.godarmed.core.framework.report.service.impl.OracleTemplateImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateFactory {


    /**
     * 创建数据库连接池
     *
     * @param driver   驱动
     * @param username     用户名
     * @param password 密码
     * @param url      url
     * @return Connection
     */
    public static JdbcTemplateInterface createMdmTemplate(String dataBaseType, String driver, String url, String username, String password) {

        // 创建MdmTemplateInterface
        JdbcTemplateInterface customTemplate = null;
        switch (dataBaseType.toUpperCase()) {

            case GlobalConstants.DATABASE_TYPE.ORACLE:
                customTemplate = new OracleTemplateImpl();
                break;

            case GlobalConstants.DATABASE_TYPE.MYSQL:
                customTemplate = new MysqlTemplateImpl();
                break;

            case GlobalConstants.DATABASE_TYPE.SQL_SERVER:
                //customTemplate = new MdmSqlserverTemplateImpl();
                break;

            default:
                break;
        }

        // 创建数据库连接
        JdbcTemplate jdbcTemplate = null;

        //创建c3p0数据库连接池
        BasicDataSource dataSource = null;
        try {
            //创建c3p0数据库连接池
            dataSource = new BasicDataSource();

            //设置注册驱动程序
            dataSource.setDriverClassName(driver);

            //设置URL
            dataSource.setUrl(url);

            //设置数据库用户名
            dataSource.setUsername(username);

            //设置数据库密码
            dataSource.setPassword(password);


            jdbcTemplate = new JdbcTemplate(dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置jdbcTemplate
        customTemplate.setJdbcTemplate(jdbcTemplate);

        //设置事务管理器
        customTemplate.setTransactionManager(dataSource);

        return customTemplate;
    }
}
