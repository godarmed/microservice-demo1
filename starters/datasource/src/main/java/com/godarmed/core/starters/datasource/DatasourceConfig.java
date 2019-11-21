package com.godarmed.core.starters.datasource;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DataBaseProperties.class})
public class DatasourceConfig {
    @Autowired
    DataBaseProperties config;

    public DatasourceConfig() {
    }

    @Bean
    public PhysicalNamingStrategy physicalNamingStrategy() {
        return new DataBaseNamingStrategy(this.config, new SpringPhysicalNamingStrategy());
    }
}
