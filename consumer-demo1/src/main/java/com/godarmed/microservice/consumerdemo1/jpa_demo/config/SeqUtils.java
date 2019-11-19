package com.godarmed.microservice.consumerdemo1.jpa_demo.config;

import com.eseasky.core.starters.redis.RedisUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class SeqUtils {
    private String currentSeqIdKey = "Current_Key";

    private Integer expireTime = 24 * 60;

    public SeqUtils() {
        super();
        initSeqUtils();
    }

    public SeqUtils(String currentSeqIdKey) {
        this.currentSeqIdKey = currentSeqIdKey;
        initSeqUtils();
    }

    public SeqUtils(String currentSeqIdKey, Integer expireTime) {
        this.currentSeqIdKey = currentSeqIdKey;
        this.expireTime = expireTime;
        initSeqUtils();
    }

    /**
     * 重置延长seqId保存时间
     */
    private void refleshSeqUtils() {
        RedisUtils redisUtils = null;
        try {
            redisUtils = new RedisUtils();
            if (redisUtils.exists(currentSeqIdKey)) {
                redisUtils.set(currentSeqIdKey, redisUtils.get(currentSeqIdKey), this.expireTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (redisUtils != null) {
                redisUtils.close();
            }
        }
    }

    /**
     * 初始化序列号生成器
     */
    private void initSeqUtils() {
        RedisUtils redisUtils = null;
        try {
            redisUtils = new RedisUtils();
            if (!redisUtils.exists(currentSeqIdKey)) {
                redisUtils.addByKey(currentSeqIdKey, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (redisUtils != null) {
                redisUtils.close();
            }
        }
    }

    /**
     * 获取序列号
     */
    public String getCurrentSeqId() {
        RedisUtils redisUtils = null;
        String seqId;
        Long currentSeqId;
        try {
            redisUtils = new RedisUtils();
            currentSeqId = redisUtils.addByKey(currentSeqIdKey, 1);
            seqId = fixSeqId(transDecimal2ThirtySix(currentSeqId));
            return seqId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            if (redisUtils != null) {
                redisUtils.close();
            }
        }
    }

    //进制转换36->10
    private static Long transThirtySix2Decimal(String source) {
        return Long.valueOf(source, 36);
    }

    //进制转换10-36
    private static String transDecimal2ThirtySix(Long source) {
        return Long.toString(source, 36);
    }

    //补全位数,默认为四位，高位补零
    private static String fixSeqId(String source) {
        return StringUtils.leftPad(source, 4, "0").toUpperCase();
    }

    public static void main(String[] args) {
        String source = "05AB";
        Long target = transThirtySix2Decimal(source);
        System.out.println(target);
        System.out.println(fixSeqId(transDecimal2ThirtySix(target)));
    }
}
