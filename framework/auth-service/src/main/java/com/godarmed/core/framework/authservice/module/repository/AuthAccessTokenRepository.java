package com.godarmed.core.framework.authservice.module.repository;


import com.godarmed.core.framework.authservice.module.model.AuthAccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthAccessTokenRepository extends CrudRepository<AuthAccessToken, String> {
    void deleteByUserName(String user_name);

    List<AuthAccessToken> findByUserName(String user_name);
}
