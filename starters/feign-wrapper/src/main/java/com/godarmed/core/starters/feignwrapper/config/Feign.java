package com.godarmed.core.starters.feignwrapper.config;

import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Feign {
    String serviceName() default "";
}
