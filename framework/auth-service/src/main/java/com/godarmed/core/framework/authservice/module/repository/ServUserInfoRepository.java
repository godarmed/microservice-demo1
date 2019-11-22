package com.godarmed.core.framework.authservice.module.repository;


import com.godarmed.core.framework.authservice.module.model.ServUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServUserInfoRepository  extends PagingAndSortingRepository<ServUserInfo, Long> ,JpaSpecificationExecutor<ServUserInfo>{


    @Override
    Optional<ServUserInfo> findById(Long aLong);

    Optional<ServUserInfo> findByUserName(String userName);

    @Override
    void deleteById(Long aLong);

    @Override
    Page<ServUserInfo> findAll(Pageable pageable);
}
