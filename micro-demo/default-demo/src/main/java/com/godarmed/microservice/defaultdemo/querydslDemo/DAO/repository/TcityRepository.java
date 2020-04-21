package com.godarmed.microservice.defaultdemo.querydslDemo.DAO.repository;

import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Tcity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Classname TcityRepository
 * @Description TODO
 * @Date 2020/4/21 10:13
 * @Created by Administrator
 */
@Repository
public interface TcityRepository extends QuerydslPredicateExecutor<Tcity>, JpaRepository<Tcity, Integer>,JpaSpecificationExecutor<Tcity> {

}

