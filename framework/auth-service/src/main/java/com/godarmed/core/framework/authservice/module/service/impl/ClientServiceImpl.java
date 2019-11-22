package com.godarmed.core.framework.authservice.module.service.impl;

import com.godarmed.core.framework.authservice.module.repository.ClientRepository;
import com.godarmed.core.starters.authserver.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public ClientDetails get(String clientId) {
        // TODO Auto-generated method stub
        return clientRepository.findByClientId(clientId);
    }

    @Override
    public void check(ClientDetails details) throws ClientRegistrationException {
        // TODO Auto-generated method stub
        return;
    }

}
