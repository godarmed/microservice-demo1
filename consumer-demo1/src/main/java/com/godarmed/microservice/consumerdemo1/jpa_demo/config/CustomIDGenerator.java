package com.godarmed.microservice.consumerdemo1.jpa_demo.config;

import com.eseasky.core.starters.redis.RedisUtils;
import lombok.Synchronized;
import org.hibernate.annotations.Synchronize;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * 自定义ID生成器
 *
 * @author zzy
 * @version 1.0
 * @date 2019-11-19
 */
public class CustomIDGenerator extends IdentityGenerator{
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        SeqUtils seqUtils = new SeqUtils("Company_Key");

        //获取
        String currentSeqId = seqUtils.getCurrentSeqId();
        return super.generate(session, currentSeqId);
    }


}
