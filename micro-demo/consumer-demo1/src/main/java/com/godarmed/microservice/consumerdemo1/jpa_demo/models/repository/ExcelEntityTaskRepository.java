package com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelEntityTaskRepository extends JpaRepository<ExcelEntityTask,String>, JpaSpecificationExecutor<ExcelEntityTask> {

}
