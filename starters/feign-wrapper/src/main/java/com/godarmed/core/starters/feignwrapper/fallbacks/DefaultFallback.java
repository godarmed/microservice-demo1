package com.godarmed.core.starters.feignwrapper.fallbacks;

import com.godarmed.core.starters.feignwrapper.exception.FallbackCreateException;
import feign.hystrix.FallbackFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DefaultFallback<T> implements FallbackFactory<T> {
    private static String hyPrefix = "hystrix";
    private Class<? extends T> type = null;
    private Class<T> current = null;

    public DefaultFallback(Class<T> current, Class<? extends T> type) {
        this.type = type;
        this.current = current;
    }

    public DefaultFallback(Class<T> current) {
        this.current = current;
    }

    @Override
    public T create(Throwable cause) {
        Class<? extends T> clazz = null;
        if (this.type != null) {
            clazz = this.type;
        } else {
            String packageName = this.current.getPackage().getName() + "." + hyPrefix;
            String className = packageName + "." + this.current.getSimpleName() + "Hystrix";

            try {
                clazz = (Class<? extends T>) Class.forName(className);
            } catch (ClassNotFoundException var7) {
                var7.printStackTrace();
                return null;
            }
        }

        try {
            return this.initByClass(clazz, cause);
        } catch (FallbackCreateException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private T initByClass(Class<? extends T> clazz, Throwable cause) throws FallbackCreateException {
        try {
            Constructor cons = clazz.getConstructor();
            T instance = (T) cons.newInstance();
            if (instance instanceof IHystrix) {
                ((IHystrix)instance).setThrowable(cause);
            }

            return instance;
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException var5) {
            var5.printStackTrace();
            throw new FallbackCreateException("1000001", var5.getMessage());
        }
    }
}
