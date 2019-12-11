package com.godarmed.core.framework.logger.mysql.repository;

import java.sql.Timestamp;
import java.util.List;

import com.godarmed.core.framework.logger.mysql.model.LoggerModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerRepository extends CrudRepository<LoggerModel, Long>,JpaSpecificationExecutor<LoggerModel>{
	List<LoggerModel> findByResponseTimeBefore(Timestamp timestamp);
}
