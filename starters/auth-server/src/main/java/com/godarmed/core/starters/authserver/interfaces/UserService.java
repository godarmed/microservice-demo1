package com.godarmed.core.starters.authserver.interfaces;


import com.godarmed.core.starters.global.entity.UserViews;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserViews getUserByName(String var1);
}
