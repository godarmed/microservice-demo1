package com.godarmed.core.starters.feignwrapper.config;

public class FeignAnnotationHandler {
    public FeignAnnotationHandler() {
    }

    public static final Feign handler(Class<?> clazz) {
        return clazz.isAnnotationPresent(Feign.class) ? (Feign)clazz.getAnnotation(Feign.class) : null;
    }
}
