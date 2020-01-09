package com.godarmed.core.framework.report.config;


import lombok.Data;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@ConfigurationProperties(
        value = "sharding.jdbc.datasource.master"
)
@Data
@Configuration
public class JdbcConfig {

    @Value("${sharding.jdbc.datasource.master.driver-class-name}")
    private String driverClassName = "com.mysql.jdbc.Driver";

    private String url;

    private String username;

    private String password;

    private int maxActive;

    private String validationQuery;

    private boolean testOnBorrow;

    private long timeBetweenEvictionRunsMillis;

    private boolean testWhileIdle;

    private long minEvictableIdleTimeMillis;

    private int initialSize;

    private int minIdle;

    private int maxWait;

    @Bean("defaultDbcpDataSource")
    public BasicDataSource defaultDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);
        return dataSource;
    }


    @Bean("myJdbcTemplate")
    public JdbcTemplate myJdbcTemplate(){
        return new JdbcTemplate(defaultDataSource());
    }


}
