package com.eseasky.core.framework.Logger.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class PropertiesUtils {
	public static <T> T transProperties(Object source, Class<T> target) {
		try {
			T targetInstance = target.newInstance();
			BeanUtils.copyProperties(source, targetInstance);
			return targetInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static <T, V> List<T> transListProperties(List<V> sources, Class<T> target) {
		return sources.stream().map(item -> {
			T targetInstance;
			try {
				targetInstance = target.newInstance();
				BeanUtils.copyProperties(item, targetInstance);
				return targetInstance;
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}
	
	public static <T> void transNotNullProperties(Object source, T target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}
	
	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
