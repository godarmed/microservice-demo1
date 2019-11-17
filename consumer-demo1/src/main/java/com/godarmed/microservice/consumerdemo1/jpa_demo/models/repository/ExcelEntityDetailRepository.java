package com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcelEntityDetailRepository extends JpaRepository<ExcelEntityDetail, Long>, JpaSpecificationExecutor<ExcelEntityDetail> {
    ExcelEntityDetail findExcelEntityByPort(String port);

    List<ExcelEntityDetail> findExcelEntityDetailByTaskId(Long taskId);
}
