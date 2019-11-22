package com.godarmed.core.starters.authclient;

import java.io.Serializable;
import java.lang.annotation.Documented;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "security.oauth2.client"
)
@Data
public class AuthClientProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ignoreUrl;
    @Value("${security.oauth2.resource.user-info-uri:/}")
    private String userService;
    @Value("${security.oauth2.client.clientId:resources}")
    private String clientId;

    public AuthClientProperties() {
    }

    public String getIgnoreUrl() {
        return this.ignoreUrl;
    }

    public String getUserService() {
        return this.userService;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setIgnoreUrl(String ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public void setUserService(String userService) {
        this.userService = userService;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
