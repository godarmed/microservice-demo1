package com.godarmed.microservice.consumerdemo1.validater_demo.config;


import org.apache.poi.ss.formula.functions.T;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NumIn.List.class)
@Documented
@Constraint(validatedBy = NumInValidator.class )
public @interface NumIn{
    String message() default "数值范围限定";

    Class<? extends Enum> enumValid();
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NumIn[] value();
    }
}