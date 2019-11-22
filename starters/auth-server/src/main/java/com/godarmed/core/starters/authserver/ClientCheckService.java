package com.godarmed.core.starters.authserver;

import com.godarmed.core.starters.authserver.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service("clientCheckService")
public class ClientCheckService implements ClientDetailsService {
    @Autowired
    private ClientService clientService;

    public ClientCheckService() {
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = this.clientService.get(clientId);
        if (details != null) {
            this.clientService.check(details);
        }

        return details;
    }
}
