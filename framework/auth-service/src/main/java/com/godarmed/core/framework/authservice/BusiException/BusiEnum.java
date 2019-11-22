package com.godarmed.core.framework.authservice.BusiException;

public enum  BusiEnum {



    SUCCESS(200,"SUCCESS"),
   
	NOT_FOUND_USER(500, "系统提示：未找到用户信息"),
	
	ARGS_NOT_ISEXIST(500, "系统提示：接收参数为空"),
	
	USERINFO_NOID(500, "系统提示：用户id为空"),
	
	USERNAME_REPEATABLE(500,"系统提示：用户名重复"), 
	
	USERINFO_IDNOTNULL(500,"系统提示：id不能为null"),

    USER_INVALID(500,"用户失效"),

    NOTDELETE(500,"管理员不可删除");

    private Integer code;

    private String message;

    //private String url;
    BusiEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
