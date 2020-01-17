package com.godarmed.microservice.consumerdemo1.validater_demo.config;

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
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};

    String name();

    int[] nums() default {};

    boolean useDefaultNums() default true;

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        NumIn[] value();
    }
}