package com.godarmed.core.starters.global.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertiesUtils{
    public PropertiesUtils() {
    }

    public static <T> T transProperties(Object source, Class<T> target) {
        try {
            T targetInstance = target.newInstance();
            BeanUtils.copyProperties(source, targetInstance);
            return targetInstance;
        } catch (IllegalAccessException | InstantiationException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T, V> List<T> transListProperties(List<V> sources, Class<T> target) {
        return (List)sources.stream().map((item) -> {
            try {
                T targetInstance = target.newInstance();
                BeanUtils.copyProperties(item, targetInstance);
                return targetInstance;
            } catch (IllegalAccessException | InstantiationException var4) {
                var4.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    public static <T> void transNotNullProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet();
        PropertyDescriptor[] var4 = pds;
        int var5 = pds.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            PropertyDescriptor pd = var4[var6];
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return (String[])emptyNames.toArray(result);
    }
}
