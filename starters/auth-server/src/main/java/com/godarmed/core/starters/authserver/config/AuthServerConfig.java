package com.godarmed.core.starters.authserver.config;

import javax.sql.DataSource;

import com.godarmed.core.starters.authserver.LockTokenService;
import com.godarmed.core.starters.authserver.UserTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@EnableAuthorizationServer
@ControllerAdvice
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("clientUserDetailService")
    private UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("clientCheckService")
    private ClientDetailsService customClientDetailsService;

    public AuthServerConfig() {
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(this.dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(this.dataSource);
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(this.dataSource);
    }

    @Bean
    public TokenEnhancer customTokenEnhancer() {
        return new UserTokenEnhancer();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.customClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.allowedTokenEndpointRequestMethods(new HttpMethod[]{HttpMethod.GET, HttpMethod.POST}).authenticationManager(this.authenticationManager).approvalStore(this.approvalStore()).tokenServices(this.createCefaultTokenService()).tokenStore(this.tokenStore()).reuseRefreshTokens(false).tokenEnhancer(this.customTokenEnhancer()).userDetailsService(this.userDetailsService).authorizationCodeServices(this.authorizationCodeServices());
        endpoints.setClientDetailsService(this.customClientDetailsService);
        endpoints.pathMapping("/oauth/confirm_access", "/oauth/confirm_access");
        endpoints.pathMapping("/oauth/error", "/oauth/error");
    }

    @Bean
    public AuthorizationServerTokenServices createCefaultTokenService() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(this.tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setClientDetailsService(this.customClientDetailsService);
        defaultTokenServices.setTokenEnhancer(this.customTokenEnhancer());
        LockTokenService lockTokenService = new LockTokenService(defaultTokenServices);
        lockTokenService.addUserDetailsService(this.userDetailsService);
        return lockTokenService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }
}
