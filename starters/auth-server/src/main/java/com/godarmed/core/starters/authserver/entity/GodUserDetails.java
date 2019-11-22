package com.godarmed.core.starters.authserver.entity;


import java.util.Collection;

import com.godarmed.core.starters.global.entity.UserViews;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class GodUserDetails extends User {
    private UserViews userViews = new UserViews();

    private static final long serialVersionUID = 4869533335170412202L;

    public GodUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public GodUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
    }

    public UserViews getUserViews() {
        return this.userViews;
    }

    public void setUserViews(UserViews userViews) {
        this.userViews = userViews;
    }
}
