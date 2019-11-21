package com.godarmed.core.starters.global.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class ValidatorUtils {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public ValidatorUtils() {
    }

    public static final <T> Map<String, String> valid(T obj) {
        Set<ConstraintViolation<T>> result = validator.validate(obj, new Class[]{Default.class});
        if (result != null && result.size() > 0) {
            HashMap<String, String> errorMap = new HashMap();
            String property = null;
            Iterator var4 = result.iterator();

            while(var4.hasNext()) {
                ConstraintViolation<T> cv = (ConstraintViolation)var4.next();
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.put(property, (String)errorMap.get(property) + "," + cv.getMessage());
                } else {
                    errorMap.put(property, cv.getMessage());
                }
            }

            if (!errorMap.isEmpty()) {
                return errorMap;
            }
        }

        return null;
    }
}