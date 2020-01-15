package com.godarmed.core.starters.rabbitmq.rpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public Client() {}

    public String getResultFromPython(RPCRequest<?> req, String queue) {
        String message = JSON.toJSONString(req);
        Object response = this.rabbitTemplate.convertSendAndReceive(queue, (Object)message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println(message);
                MessageProperties props = message.getMessageProperties();
                props.setHeader("header", "1.0.0");
                props.setExpiration(String.valueOf(300000));
                return message;
            }
        });
        if (response != null) {
            System.out.println("response is :");
            System.out.println(new String((byte[])response));
            return new String((byte[])response);
        } else {
            return null;
        }
    }

    public JSONArray getListResultFromPython(RPCRequest<?> req, String queue) {
        String resp = this.getResultFromPython(req, queue);
        return resp != null ? JSONArray.parseArray(resp) : null;
    }

    public JSONObject getObjectResultFromPython(RPCRequest<?> req, String queue) {
        String resp = this.getResultFromPython(req, queue);
        return resp != null ? JSONObject.parseObject(resp) : null;
    }

    public <T> T RPCRequestReturn(RPCRequest<?> req, String queue, Class<T> obj) {
        try {
            String resp = this.getResultFromPython(req, queue);
            if (resp != null) {
                return JSONObject.parseObject(resp, obj);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return null;
    }
}
