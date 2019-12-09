package com.godarmed.microservice.consumerdemo1.jpa_demo.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

/**
 * 自定义ID生成器
 *o
 * @author zzy
 * @version 1.0
 * @date 2019-11-19
 */
public class CustomIDGenerator extends UUIDGenerator {
    final static String taskId = "Task_Id_Key";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        //获取
        String currentSeqId = SeqUtils.getCurrentSeqId(taskId);
        if (currentSeqId != null) {
            return (Serializable)currentSeqId;
        }
        return super.generate(session, object);
    }


}
