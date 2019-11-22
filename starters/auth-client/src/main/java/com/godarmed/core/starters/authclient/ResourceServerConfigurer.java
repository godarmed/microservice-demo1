package com.godarmed.core.starters.authclient;

import com.godarmed.core.starters.authclient.exception.AccessDeniedException;
import com.godarmed.core.starters.authclient.exception.TokenInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@EnableConfigurationProperties({AuthClientProperties.class})
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Autowired
    private AuthClientProperties authClientProperties;

    public ResourceServerConfigurer() {
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] ignore = new String[0];
        if (this.authClientProperties.getIgnoreUrl() != null && !"".equals(this.authClientProperties.getIgnoreUrl())) {
            ignore = this.authClientProperties.getIgnoreUrl().split(",");
        }

        ((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)((HttpSecurity)((HttpSecurity)http.csrf().disable()).exceptionHandling().authenticationEntryPoint(new TokenInvalidException()).and()).authorizeRequests().antMatchers(ignore)).permitAll().anyRequest()).authenticated().and()).httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new TokenInvalidException()).accessDeniedHandler(new AccessDeniedException()).tokenServices(new UserInfoTokenServices(this.authClientProperties.getUserService(), this.authClientProperties.getClientId())).resourceId(this.authClientProperties.getClientId());
    }
}

