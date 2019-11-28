package com.godarmed.core.starters.authserver;

import com.godarmed.core.starters.authserver.entity.GodUserDetails;
import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.core.starters.redis.lock.SimpleRedisLock;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

public class LockTokenService implements AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices, InitializingBean {
    private DefaultTokenServices proxyTokenService = null;
    private static final String REDIS_PREFIX = "OAUTH2_TOKEN_";

    public LockTokenService(DefaultTokenServices tokenStore) {
        this.proxyTokenService = tokenStore;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.proxyTokenService.afterPropertiesSet();
    }

    @Override
    public boolean revokeToken(String tokenValue) {
        return this.proxyTokenService.revokeToken(tokenValue);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        return this.proxyTokenService.loadAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return this.proxyTokenService.readAccessToken(accessToken);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        GodUserDetails user = (GodUserDetails) authentication.getPrincipal();
        SimpleRedisLock lock = null;
        if (user != null) {
            String name = user.getUsername();

            OAuth2AccessToken var5;
            try {
                lock = new SimpleRedisLock("OAUTH2_TOKEN_" + name, new RedisUtils());
                if (!lock.acquireLock()) {
                    throw new InvalidGrantException("get token error");
                }

                var5 = this.proxyTokenService.createAccessToken(authentication);
            } catch (Exception var9) {
                var9.printStackTrace();
                throw new InvalidGrantException(var9.getMessage());
            } finally {
                if (lock != null) {
                    lock.releaseLockAndClose();
                }

            }

            return var5;
        } else {
            throw new InvalidGrantException("get token error");
        }
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshToken, TokenRequest tokenRequest) throws AuthenticationException {
        return this.proxyTokenService.refreshAccessToken(refreshToken, tokenRequest);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return this.proxyTokenService.getAccessToken(authentication);
    }

    public void addUserDetailsService(UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper(userDetailsService));
            this.proxyTokenService.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        }

    }
}
