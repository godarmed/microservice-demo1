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
public class CustomIDGenerator extends IdentityGenerator {

    final static String currentNumKey = "CurrentNum-Key";

    static{

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        //获取
        String currentSeqId = addAndGetCurrentSeqId();
        return super.generate(session, currentSeqId);
    }

    public void initCurrentSeqId(){
        return;
    }

    public String getCurrentSeqId(){
        RedisUtils redisUtils = null;
        String seqId;
        Long currentSeqId;
        try {
            redisUtils = new RedisUtils();
            seqId = redisUtils.get(currentNumKey);
            if(seqId != null){
                currentSeqId = Long.valueOf(seqId);
            }else{
                initCurrentSeqId();
                return getCurrentSeqId();
            }
            seqId = transDecimal2ThirtySix(currentSeqId);
            return seqId;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(redisUtils!=null){
                redisUtils.close();
            }
        }
        return null;
    }

    public String addAndGetCurrentSeqId(){
        RedisUtils redisUtils = null;
        String seqId;
        try {
            redisUtils = new RedisUtils();
            synchronized(this){
                Long currentSeqId = Long.valueOf(redisUtils.get(currentNumKey));
                currentSeqId++;
                seqId = transDecimal2ThirtySix(currentSeqId);
            }
            return seqId;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(redisUtils!=null){
                redisUtils.close();
            }
        }
    }

    //进制转换
    public static Long transThirtySix2Decimal(String source){
        return Long.valueOf(source,36);
    }

    //进制转换
    public static String transDecimal2ThirtySix(Long source){
        return Long.toString(source,36);
    }

    public static void main(String[] args) {
        String source = "05AB";
        Long target = transThirtySix2Decimal(source);
        System.out.println(target);
        System.out.println(transDecimal2ThirtySix(target));
    }
}
