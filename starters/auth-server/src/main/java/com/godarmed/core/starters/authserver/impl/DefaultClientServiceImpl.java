package com.godarmed.core.starters.authserver.impl;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.godarmed.core.starters.authserver.interfaces.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@ConditionalOnMissingBean({ClientService.class})
public class DefaultClientServiceImpl implements ClientService {
    private static final Logger log = LogManager.getLogger(DefaultClientServiceImpl.class);

    public DefaultClientServiceImpl() {
    }

    @Override
    public ClientDetails get(String clientId) {
        return new ClientDetails() {
            @Override
            public boolean isSecretRequired() {
                return false;
            }

            @Override
            public boolean isScoped() {
                return false;
            }

            @Override
            public boolean isAutoApprove(String scope) {
                return false;
            }

            @Override
            public Set<String> getScope() {
                return null;
            }

            @Override
            public Set<String> getResourceIds() {
                return null;
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return null;
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return null;
            }

            @Override
            public String getClientSecret() {
                return null;
            }

            @Override
            public String getClientId() {
                DefaultClientServiceImpl.log.info("1111");
                return "1";
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return null;
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return null;
            }
        };
    }

    @Override
    public void check(ClientDetails details) throws ClientRegistrationException {
    }
}
