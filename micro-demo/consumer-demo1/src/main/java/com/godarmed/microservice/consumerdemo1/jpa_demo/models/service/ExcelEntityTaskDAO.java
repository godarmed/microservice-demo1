package com.godarmed.microservice.consumerdemo1.jpa_demo.models.service;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ExcelEntityTaskDAO {


    List<ExcelEntityTask> findAll();


    List<ExcelEntityTask> findAll(Sort sort);


    List<ExcelEntityTask> findAllById(Iterable<String> ids);


    <S extends ExcelEntityTask> List<S> saveAll(Iterable<S> entities);


    void flush();


    <S extends ExcelEntityTask> S saveAndFlush(S entity);


    void deleteInBatch(Iterable<ExcelEntityTask> entities);


    void deleteAllInBatch();


    ExcelEntityTask getOne(String id);


    List<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec);


    Page<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec, Pageable pageable);


    List<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec, Sort sort);


    long count(Specification<ExcelEntityTask> spec);


    <S extends ExcelEntityTask> S save(S entity);


    Optional<ExcelEntityTask> findById(String id);


    boolean existsById(String id);


    long count();


    void deleteById(String id);


    void delete(ExcelEntityTask entity);


    void deleteAll(Iterable<? extends ExcelEntityTask> entities);


    void deleteAll();
}
