package com.godarmed.core.starters.feignwrapper;

import com.godarmed.core.starters.feignwrapper.config.Feign;
import com.godarmed.core.starters.feignwrapper.config.FeignAnnotationHandler;
import com.godarmed.core.starters.feignwrapper.config.FeignConfig;
import com.godarmed.core.starters.feignwrapper.config.FeignProperties;
import com.godarmed.core.starters.feignwrapper.exception.FeignClientCreateException;
import com.godarmed.core.starters.feignwrapper.fallbacks.DefaultFallback;
import com.godarmed.core.starters.feignwrapper.interceptor.HeaderCopyInterceptor;
import com.godarmed.core.starters.feignwrapper.logger.LoggerRecordService;
import com.godarmed.core.starters.feignwrapper.logger.Slf4jRecordLogger;
import com.godarmed.core.starters.feignwrapper.targets.DefaultDynamicTarget;
import com.godarmed.core.starters.global.entity.ReflexEntity;
import com.godarmed.core.starters.global.utils.ReflexUtils;
import com.godarmed.core.starters.global.utils.SpringUtils;
import com.godarmed.core.starters.rabbitmq.rpc.client.Client;
import com.google.common.base.Strings;
import feign.*;
import feign.Feign.Builder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

public class FeignBuilder<T> {
    private static final Logger log = LogManager.getLogger(FeignBuilder.class);
    private Builder builder;
    private Decoder decoder;
    private Encoder encoder;
    private Client client;
    private Contract contract;
    private FeignProperties config;
    private feign.Logger logger;
    private feign.Logger.Level level;
    private Retryer retryer;
    private Request.Options options;
    private FallbackFactory<T> fallbackFactory;
    private List<String> inheritHeaders;
    private List<RequestInterceptor> interceptors;
    Field[] fs;
    private Map<String, Object> staticHeaders;
    private Class<T> targetClient;
    private LoggerRecordService loggerRecordService;
    private Map<String, Object> defaultValue;

    public static <E> FeignBuilder<E> clientBuilder(Class<E> targetClient) {
        return new FeignBuilder(targetClient);
    }

    private FeignBuilder(Class<T> targetClient) {
        this.level = feign.Logger.Level.FULL;
        this.inheritHeaders = new ArrayList(Arrays.asList("token", "Authorization", "access_token"));
        this.interceptors = new ArrayList();
        this.fs = FeignBuilder.class.getDeclaredFields();
        this.staticHeaders = new HashMap();
        this.targetClient = null;
        this.defaultValue = new HashMap();
        this.targetClient = targetClient;
    }

    public FeignBuilder<T> addDefault(String name, Object value) {
        this.defaultValue.put(name, value);
        return this;
    }

    public FeignBuilder<T> builder(Builder builder) {
        if (builder == null) {
            builder = (Builder) SpringUtils.getBean(Builder.class);
        } else {
            this.builder = builder;
        }

        return this;
    }

    public FeignBuilder<T> decoder(Decoder decoder) {
        this.decoder = decoder;
        return this;
    }

    public FeignBuilder<T> encoder(Encoder encoder) {
        this.encoder = encoder;
        return this;
    }

    public FeignBuilder<T> client(Client client) {
        this.client = client;
        return this;
    }

    public FeignBuilder<T> contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public FeignBuilder<T> options(Request.Options options) {
        this.options = options;
        return this;
    }

    public FeignBuilder<T> logger(feign.Logger logger) {
        this.logger = logger;
        return this;
    }

    public FeignBuilder<T> loggerLevel(feign.Logger.Level level) {
        this.level = level;
        return this;
    }

    public FeignBuilder<T> requestInterceptor(RequestInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    public FeignBuilder<T> copyHeaders(List<String> inheritHeaders) {
        this.inheritHeaders = inheritHeaders;
        return this;
    }

    public FeignBuilder<T> addCopyHeaders(String... needCopyHeaders) {
        String[] var2 = needCopyHeaders;
        int var3 = needCopyHeaders.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String header = var2[var4];
            this.inheritHeaders.add(header);
        }

        return this;
    }

    public FeignBuilder<T> recordService(LoggerRecordService service) {
        this.loggerRecordService = service;
        return this;
    }

    public FeignBuilder<T> addCopyHeaders(List<String> needCopyHeaders) {
        if (needCopyHeaders != null) {
            Iterator var2 = needCopyHeaders.iterator();

            while(var2.hasNext()) {
                String header = (String)var2.next();
                if (!this.inheritHeaders.contains(header)) {
                    this.inheritHeaders.add(header);
                }
            }
        }

        return this;
    }

    public FeignBuilder<T> fallbackFactory(FallbackFactory<T> factory) {
        this.fallbackFactory = factory;
        return this;
    }

    public FeignBuilder<T> addStaticHeaders(String key, Object value) {
        this.staticHeaders.put(key, value);
        return this;
    }

    public FeignBuilder<T> addStaticHeaders(Map<String, Object> staticHeaders) {
        if (staticHeaders != null) {
            Set<String> keys = staticHeaders.keySet();
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                this.staticHeaders.put(key, staticHeaders.get(key));
            }
        }

        return this;
    }

    private Builder generatorBuilder() throws IllegalArgumentException, IllegalAccessException {
        this.parseConfig();
        List<String> defaultInitnames = Arrays.asList("builder", "decoder", "encoder", "client", "contract", "options");
        List<ReflexEntity> entities = ReflexUtils.reflex2Entity(this);
        Iterator var3 = entities.iterator();

        while(var3.hasNext()) {
            ReflexEntity entity = (ReflexEntity)var3.next();
            if (defaultInitnames.contains(entity.getName())) {
                this.setValue(entity.getName(), this.getDefaultBean(entity.getValue(), entity.getType()));
            }
        }

        Optional<RequestInterceptor> headCopyOpt = this.interceptors.stream().filter((item) -> {
            return item.getClass().getSimpleName().equals("HeaderCopyInterceptor");
        }).findFirst();
        if (!headCopyOpt.isPresent()) {
            this.interceptors.add(new HeaderCopyInterceptor(this.config, this.inheritHeaders));
        }

        this.logger = this.getLogger();
        return this.builder.client((feign.Client) this.client).decoder(this.decoder).encoder(this.encoder).contract(this.contract).options(this.options).requestInterceptors(this.interceptors).logger(this.logger).logLevel(this.level);
    }

    private void parseConfig() {
        this.config = (FeignProperties)this.getDefaultBean((Object)null, FeignProperties.class);
        if (this.config != null && this.config.getClients() != null && !this.config.getClients().isEmpty() && (this.retryer == null || this.options == null)) {
            Map<String, FeignConfig> clientConfigs = this.transList2MapConfig(this.config.getClients());
            FeignConfig validConfig = null;
            if (clientConfigs.containsKey("default")) {
                validConfig = (FeignConfig)clientConfigs.get("default");
            }

            String serviceName = this.getServiceFromClient();
            if (clientConfigs.containsKey(serviceName)) {
                validConfig = (FeignConfig)clientConfigs.get(serviceName);
            } else if (clientConfigs.containsKey(this.targetClient.getSimpleName())) {
                validConfig = (FeignConfig)clientConfigs.get(this.targetClient.getSimpleName());
            }

            if (validConfig != null) {
                this.setRetry(validConfig);
                this.setOptions(validConfig);
            }
        }

    }

    private Map<String, FeignConfig> transList2MapConfig(List<FeignConfig> configs) {
        Map<String, FeignConfig> mapConfog = new HashMap();
        Iterator var3 = configs.iterator();

        while(var3.hasNext()) {
            FeignConfig configItem = (FeignConfig)var3.next();
            mapConfog.put(Strings.isNullOrEmpty(configItem.getServiceName()) ? configItem.getClassName() : configItem.getServiceName(), configItem);
        }

        return mapConfog;
    }

    private void setRetry(FeignConfig validConfig) {
        if (this.retryer == null && validConfig.getMaxAttempts() > 1) {
            this.retryer = new Retryer.Default(validConfig.getPeriod(), validConfig.getMaxPeriod(), validConfig.getMaxAttempts());
        }

    }

    private void setOptions(FeignConfig validConfig) {
        if (this.options == null) {
            this.options = new Request.Options(validConfig.getConnectTimeout(), validConfig.getReadTimeout());
        }

    }

    private feign.Logger getLogger() {
        if (this.logger == null) {
            this.loggerRecordService = this.loggerRecordService == null ? (LoggerRecordService)SpringUtils.getBean(LoggerRecordService.class) : this.loggerRecordService;
            return new Slf4jRecordLogger(this.targetClient, this.loggerRecordService);
        } else {
            return this.logger;
        }
    }

    private void setValue(String fieldName, Object value) {
        try {
            Field[] var3 = this.fs;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (field.getName().equals(fieldName)) {
                    field.set(this, value);
                    break;
                }
            }
        } catch (Exception var7) {
            log.error(var7.getMessage());
        }

    }

    private <E> E getDefaultBean(Object value, Class<E> clazz) {
        if (value == null) {
            try {
                return this.defaultValue.containsKey(clazz.getSimpleName()) ? (E) this.defaultValue.get(clazz.getSimpleName()) : SpringUtils.getBean(clazz);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return (E) value;
        }
    }

    public T toDefaultClient() throws FeignClientCreateException {
        try {
            Builder builder = this.generatorBuilder();
            return builder instanceof feign.hystrix.HystrixFeign.Builder ? ((feign.hystrix.HystrixFeign.Builder)builder).target(this.builderTarget((String)null), (T) new DefaultFallback(this.targetClient)) : builder.target(this.builderTarget((String)null));
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new FeignClientCreateException(var2.getMessage());
        }
    }

    public T toDefaultClient(String serviceName) throws FeignClientCreateException {
        try {
            Builder builder = this.generatorBuilder();
            this.fallbackFactory = (FallbackFactory)(this.fallbackFactory != null ? this.fallbackFactory : new DefaultFallback(this.targetClient));
            return builder instanceof feign.hystrix.HystrixFeign.Builder ? ((feign.hystrix.HystrixFeign.Builder)builder).target(this.builderTarget(serviceName), this.fallbackFactory) : builder.target(this.builderTarget(serviceName));
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new FeignClientCreateException(var3.getMessage());
        }
    }

    private Target<T> builderTarget(String serviceName) {
        DefaultDynamicTarget<T> defaultTarget = new DefaultDynamicTarget();
        defaultTarget.setType(this.targetClient);
        if (Strings.isNullOrEmpty(serviceName)) {
            defaultTarget.setServiceName(this.getServiceFromClient());
        } else {
            defaultTarget.setServiceName(serviceName);
        }

        defaultTarget.setUrl(this.config.getProtocol() + "://" + this.config.getGateway() + "/" + defaultTarget.getServiceName());
        defaultTarget.setHeaders(this.staticHeaders);
        return defaultTarget;
    }

    private String getServiceFromClient() {
        Feign params = FeignAnnotationHandler.handler(this.targetClient);
        return params != null ? params.serviceName() : null;
    }
}
