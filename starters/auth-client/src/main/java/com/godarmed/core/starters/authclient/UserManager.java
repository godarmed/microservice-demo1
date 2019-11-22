package com.godarmed.core.starters.authclient;

import com.alibaba.fastjson.JSON;
import java.util.Map;

import com.godarmed.core.starters.global.entity.UserViews;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class UserManager {
    public UserManager() {
    }

    public static UserViews currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
            Map<String, Object> object = (Map)oAuth2Authentication.getUserAuthentication().getDetails();
            object = (Map)object.get("principal");
            UserViews data = (UserViews)JSON.parseObject(JSON.toJSONString(object.get("userViews")), UserViews.class);
            return data;
        } else {
            return null;
        }
    }
}

