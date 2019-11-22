package com.godarmed.core.starters.authserver.interfaces;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
    ClientDetails get(String var1);

    void check(ClientDetails var1) throws ClientRegistrationException;
}