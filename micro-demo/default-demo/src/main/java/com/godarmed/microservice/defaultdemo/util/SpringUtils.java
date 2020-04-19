package com.godarmed.microservice.defaultdemo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;

/**
 * 获取Spring管理的Bean
 */
@Configuration
public class SpringUtils implements ApplicationContextAware {
    private static final Logger log = LogManager.getLogger(SpringUtils.class);
    private static ApplicationContext applicationContext;

    public SpringUtils() {
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception var2) {
            log.error("getBean error for: " + clazz.getSimpleName());
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> Collection<T> getBeanOfType(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        return beans == null ? null : beans.values();
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }
}

