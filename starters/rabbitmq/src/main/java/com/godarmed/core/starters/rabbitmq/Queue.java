package com.godarmed.core.starters.rabbitmq;

import lombok.Data;

@Data
public class Queue {
    private String name;
    private String exchangeName;
    private String bindName;
    private String type = "topic";
}
