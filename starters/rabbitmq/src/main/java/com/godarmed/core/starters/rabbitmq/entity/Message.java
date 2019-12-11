package com.godarmed.core.starters.rabbitmq.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Message<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String queueName;
    private Timestamp createTime;
    private String exchange = "topic.exchange";
    private Map<String, Object> context = new HashMap<>();
    private List<T> messages;

    public void addMessage(T message) {
        if (this.getMessages() == null) {
            this.setMessages(new ArrayList<>());
        }

        this.getMessages().add(message);
    }

    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }
}
