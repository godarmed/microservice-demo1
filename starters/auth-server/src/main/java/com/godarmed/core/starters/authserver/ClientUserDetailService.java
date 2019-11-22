package com.godarmed.core.starters.authserver;

import com.godarmed.core.starters.authserver.entity.GodUserDetails;
import com.godarmed.core.starters.authserver.interfaces.UserService;
import com.godarmed.core.starters.global.entity.UserViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("clientUserDetailService")
public class ClientUserDetailService implements UserDetailsService {
    @Autowired
    UserService userService;

    public ClientUserDetailService() {
    }

    @Override
    public GodUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserViews user = this.userService.getUserByName(username);
        if (user != null) {
            GodUserDetails details = new GodUserDetails(username, user.getPassword(), new ArrayList());
            user.setPassword((String) null);
            details.setUserViews(user);
            return details;
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}