package com.godarmed.core.starters.global.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReflexEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Class<?> type;
    private String name;
    private Object value;

    public ReflexEntity() {
    }

    public ReflexEntity(Class<?> type, String name, Object value) {
        this.setType(type);
        this.setName(name);
        this.setValue(value);
    }
}
