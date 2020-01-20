package com.godarmed.microservice.consumerdemo1.jpa_demo.models.service;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ExcelEntityDetailDAO {

    List<ExcelEntityDetail> findAll();


    List<ExcelEntityDetail> findAll(Sort sort);


    List<ExcelEntityDetail> findAllById(Iterable<Long> ids);


    <S extends ExcelEntityDetail> List<S> saveAll(Iterable<S> entities);


    void flush();


    <S extends ExcelEntityDetail> S saveAndFlush(S entity);


    void deleteInBatch(Iterable<ExcelEntityDetail> entities);


    void deleteAllInBatch();


    ExcelEntityDetail getOne(Long id);


    List<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec);


    Page<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec, Pageable pageable);


    List<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec, Sort sort);


    long count(Specification<ExcelEntityDetail> spec);


    <S extends ExcelEntityDetail> S save(S entity);


    Optional<ExcelEntityDetail> findById(Long id);


    boolean existsById(Long id);


    long count();


    void deleteById(Long id);


    void delete(ExcelEntityDetail entity);


    void deleteAll(Iterable<? extends ExcelEntityDetail> entities);


    void deleteAll();


    ExcelEntityDetail findExcelEntityById(Long id);


    List<ExcelEntityDetail> findExcelEntityByTaskId(Long id);
}
