package com.godarmed.core.starters.global.utils;

import com.godarmed.core.starters.global.entity.ReflexEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflexUtils {
    public ReflexUtils() {
    }

    public static final <T> void reflex(T entity, ReflectInvoke action) throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String fieldName = null;
        Class<?> type = null;
        Field[] var6 = fields;
        int var7 = fields.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Field field = var6[var8];
            fieldName = field.getName();
            type = field.getType();
            field.setAccessible(true);
            action.invoke(type, fieldName, field.get(entity));
        }

    }

    public static final <T> List<ReflexEntity> reflex2Entity(T entity) throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<ReflexEntity> entities = new ArrayList();
        Field[] var4 = fields;
        int var5 = fields.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            field.setAccessible(true);
            entities.add(new ReflexEntity(field.getType(), field.getName(), field.get(entity)));
        }

        return entities;
    }

    public static final <T> List<ReflexEntity> reflex2EntityAndSuperClass(T entity) throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> clazz = entity.getClass();

        ArrayList entities;
        for (entities = new ArrayList(); clazz != null; clazz = clazz.getSuperclass()) {
            Field[] var3 = clazz.getDeclaredFields();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                field.setAccessible(true);
                entities.add(new ReflexEntity(field.getType(), field.getName(), field.get(entity)));
            }
        }

        return entities;
    }

    public static final <T> T invoke(Object instance, String methodName, Object[] args, Class<T> resutnType) throws Exception {
        Class<?>[] argType = null;
        if (args != null) {
            argType = new Class[args.length];

            for (int i = 0; i < args.length; ++i) {
                argType[i] = args.getClass();
            }
        }

        Class<?> instClazz = instance.getClass();
        Method method = argType != null ? instClazz.getMethod(methodName, argType) : instClazz.getMethod(methodName);
        T ret = null;
        if (argType != null) {
            ret = (T)method.invoke(instance, args);
        } else {
            ret = (T)method.invoke(instance);
        }

        return resutnType.isAssignableFrom(Void.class) ? null : ret;
    }

    public static final void invoke(Object instance, String methodName, Object[] args) throws Exception {
        invoke(instance, methodName, args, Void.class);
    }
}

