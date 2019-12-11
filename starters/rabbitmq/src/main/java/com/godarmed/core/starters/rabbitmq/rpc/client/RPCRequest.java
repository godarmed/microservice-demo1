package com.godarmed.core.starters.rabbitmq.rpc.client;

import lombok.Data;

@Data
public class RPCRequest<T> {
    private String apiName = null;
    private T params = null;
}
