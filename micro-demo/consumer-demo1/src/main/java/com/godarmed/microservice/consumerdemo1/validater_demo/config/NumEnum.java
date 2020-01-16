package com.godarmed.microservice.consumerdemo1.validater_demo.config;

public enum NumEnum {
    /**
     * 默认枚举值
     */
    NUM1(1),
    NUM2(2),
    NUM3(3),
    NUM4(4),
    NUM5(5);

    private Integer num;

    NumEnum(Integer num){
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }

    public static boolean isInEnum(Integer currentNum) {
        for (NumEnum num : NumEnum.values()) {
            if (num.getNum().equals(currentNum)) {
                return true;
            }
        }
        return false;
    }
}
