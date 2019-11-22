package com.godarmed.core.framework.authservice.module.repository;

import com.godarmed.core.framework.authservice.module.model.ServClientDetails;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ServClientDetails, Long>{
	ServClientDetails findByClientId(String clientId);
}
