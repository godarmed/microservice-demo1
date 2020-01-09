package com.godarmed.core.framework.report.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

public abstract class JdbcTemplateAbstract implements JdbcTemplateInterface {

    /**
     * jdbcTemplate
     */
    protected JdbcTemplate jdbcTemplate;

    protected DataSourceTransactionManager dataSourceTransactionManager;

    protected ThreadLocal<TransactionStatus> status = new ThreadLocal<TransactionStatus>();

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void setTransactionManager(DataSource dataSource) {
        this.dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    }

    @Override
    public DataSourceTransactionManager getTransactionManager() {
        return dataSourceTransactionManager;
    }


    @Override
    public void beginTransaction(){
        try {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status.set(dataSourceTransactionManager.getTransaction(def));// 返回事务对象
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void endTransaction() {
        try {
            dataSourceTransactionManager.commit(status.get());
        } catch (Exception e) {
            e.printStackTrace();
            dataSourceTransactionManager.rollback(status.get());
        }finally {
            status.remove();
        }
    }
}