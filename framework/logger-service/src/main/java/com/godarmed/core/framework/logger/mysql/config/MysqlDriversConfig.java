//package com.eseasky.core.framework.Logger.drivers.mysql.config;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.persistence.EntityManager;
//import javax.sql.DataSource;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@ConditionalOnProperty(prefix="logger", name="persistence.drivers", havingValue="mysql", matchIfMissing=true)
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(  
//        entityManagerFactoryRef="entityManagerFactoryLogger",  
//        transactionManagerRef="transactionManagerLogger",
//        basePackages= "com.eseasky.core.framework.Logger.drivers.mysql.repository"
//)
//public class MysqlDriversConfig {
//	
//	@Resource
//    private DataSource datasource;
// 
//    @Bean(name = "entityManagerLogger")
//    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
//    }
// 
//    @Resource
//    private JpaProperties jpaProperties;
// 
//    private Map<String, Object> getVendorProperties() {
//        return jpaProperties.getHibernateProperties(new HibernateSettings());
//    }
// 
//    /**
//     * 设置实体类所在位置
//     */
//    @Primary
//    @Bean(name = "entityManagerFactoryLogger")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
//    	return builder
//                .dataSource(datasource)
//                .packages("com.eseasky.core.framework.Logger.drivers.mysql.model")
//                .persistenceUnit("logSavePersistenceUnit")
//                .properties(getVendorProperties())
//                .build();
//    }
//    
//    @Bean(name = "transactionManagerLogger")
//    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
//        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
//    }
//
//}