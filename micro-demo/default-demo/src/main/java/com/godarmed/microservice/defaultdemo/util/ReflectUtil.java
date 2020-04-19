package com.godarmed.microservice.defaultdemo.util;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class ReflectUtil {
    /**
     * 转换List<Object>为List<List<Object>>
     * @param source       要转换的对象列表
     * @param fieldList    要转换的属性名称
     * @return
     */
    public static  List<List<Object>> acquireFieldList(List<?> source, List<String> fieldList) {
        List<List<Object>> target = new ArrayList<>();
        for (Object obj : source) {
            List<Object> temp = acquireFieldList(obj,fieldList);
            target.add(temp);
        }
        return target;
    }

    /**
     * 转换对象的所有属性（包括父类属性）为Map<String,Object>
     * @param obj   要转换的对象
     * @return
     */
    public static <T> Map<String,Object> acquireFieldMap(T obj) {
        return acquireFieldMap(obj,new ArrayList<String>());
    }

    /**
     * 转换对象的部分属性（包括父类属性）为Map<String,Object>
     * @param obj               要转换的对象
     * @param excludeFields     要排除的属性名称
     * @return
     */
    public static <T> Map<String,Object> acquireFieldMap(T obj, List<String> excludeFields) {
        Validate.isTrue(obj != null, "The object must not be null");
        final Class<?> cls = obj.getClass();
        final Map<String,Object> fieldMap = new HashMap<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                if(!excludeFields.contains(field.getName())){
                    try {
                        //获取访问权限
                        field.setAccessible(true);
                        fieldMap.put(field.getName(),field.get(obj));
                    } catch (IllegalAccessException e) {
                        log.error("反射获取属性值失败，失败原因[{}]",e.getLocalizedMessage());
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fieldMap;
    }

    /**
     * 转换对象的部分属性（包括父类属性）为Map<String,Object>
     * @param obj               要转换的对象
     * @param fieldList         要获取的属性名称
     * @return
     */
    public static <T> List<Object> acquireFieldList(T obj, List<String> fieldList) {
        Validate.isTrue(obj != null, "The object must not be null");
        final Class<?> cls = obj.getClass();
        final Map<String,Object> fieldMap = new HashMap<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                if(fieldList.contains(field.getName())){
                    try {
                        //获取访问权限
                        field.setAccessible(true);
                        fieldMap.put(field.getName(),field.get(obj));
                    } catch (IllegalAccessException e) {
                        log.error("反射获取属性值失败，失败原因[{}]",e.getLocalizedMessage());
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fieldList.stream().map(fieldMap::get).collect(Collectors.toList());
    }

    /**
     * 转换Map<String,Object>中所有属性为List<Object>
     * @param source       要转换的对象
     * @return
     */
    public static  List<Object> transFieldMap2List(Map<String,?> source) {
        final List<Object> objList = new ArrayList<>();
        objList.addAll(source.values());
        return objList;
    }

    /**
     * 转换Map<String,Object>中部分属性为List<Object>
     * @param source       要转换的对象
     * @param fieldList    要转换的属性名称
     * @return
     */
    public static List<Object> transFieldMap2List(Map<String,?> source, List<String> fieldList) {
        final List<Object> objList = new ArrayList<>();
        for (String o : fieldList) {
            objList.add(source.get(o));
        }
        return objList;
    }
}
