package com.godarmed.core.starters.authserver;

import com.godarmed.core.starters.authserver.entity.GodUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class UserTokenEnhancer implements TokenEnhancer {
    public UserTokenEnhancer() {
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        GodUserDetails user = (GodUserDetails) authentication.getPrincipal();
        if (user != null) {
            Map<String, Object> additionalInfo = new HashMap();
            additionalInfo.put("userView", user.getUserViews());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }

        return accessToken;
    }
}

