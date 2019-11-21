package com.godarmed.core.starters.datasource;



import com.godarmed.core.starters.global.utils.ReflexUtils;
import com.google.common.collect.Lists;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class DynamicQueryBuilder<T> {
    private T request;
    private List<Predicate> predicates = Lists.newArrayList();
    private List<String> excludes = Lists.newArrayList();

    public DynamicQueryBuilder(T request) {
        this.request = request;
    }

    public DynamicQueryBuilder(T request, String[] excludes) {
        this.request = request;
        this.excludes = Arrays.asList(excludes);
    }

    public List<Predicate> builder(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder) throws IllegalArgumentException, IllegalAccessException {
        if (this.request != null) {
            ReflexUtils.reflex(this.request, (type, fieldName, value) -> {
                if (!this.excludes.contains(fieldName)) {
                    if (String.class.isAssignableFrom(type) && value != null && !"".equals(value)) {
                        this.predicates.add(this.string(root, query, builder, fieldName, String.valueOf(value)));
                    }

                    if (Integer.class.isAssignableFrom(type) && value != null) {
                        this.predicates.add(this.integer(root, query, builder, fieldName, (Integer) value));
                    }

                    if (Long.class.isAssignableFrom(type) && value != null) {
                        this.predicates.add(this.longType(root, query, builder, fieldName, (Long) value));
                    }

                    Predicate predicate;
                    if (Timestamp[].class.isAssignableFrom(type) && value != null) {
                        predicate = this.timestamp(root, query, builder, fieldName, (Timestamp[]) ((Timestamp[]) value));
                        if (predicate != null) {
                            this.predicates.add(predicate);
                        }
                    } else if (Timestamp.class.isAssignableFrom(type) && value != null) {
                        predicate = this.timestamp(root, query, builder, fieldName, new Timestamp[]{(Timestamp) value, null});
                        if (predicate != null) {
                            this.predicates.add(predicate);
                        }
                    }

                }
            });
            return this.predicates;
        } else {
            return null;
        }
    }

    private Predicate string(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder, String key, String value) {
        return builder.like(root.get(key), "%" + value + "%");
    }

    private Predicate integer(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder, String key, Integer value) {
        return builder.equal(root.get(key), value);
    }

    private Predicate longType(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder, String key, Long value) {
        return builder.equal(root.get(key), value);
    }

    private Predicate timestamp(From<?, ?> root, CriteriaQuery<?> query, CriteriaBuilder builder, String key, Timestamp[] values) {
        if (values[0] == null && values[1] != null) {
            return builder.lessThan(root.get(key), values[1]);
        } else if (values[1] == null && values[0] != null) {
            return builder.greaterThan(root.get(key), values[0]);
        } else {
            return values[1] != null && values[0] != null ? builder.between(root.get(key), values[0], values[1]) : null;
        }
    }
}

