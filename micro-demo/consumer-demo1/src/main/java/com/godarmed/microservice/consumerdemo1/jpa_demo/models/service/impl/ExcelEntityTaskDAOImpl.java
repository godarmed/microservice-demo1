package com.godarmed.microservice.consumerdemo1.jpa_demo.models.service.impl;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityTaskRepository;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.service.ExcelEntityTaskDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelEntityTaskDAOImpl implements ExcelEntityTaskDAO{
    
    @Autowired
    ExcelEntityTaskRepository repository;

    @Override
    public List<ExcelEntityTask> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ExcelEntityTask> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public List<ExcelEntityTask> findAllById(Iterable<String> iterable) {
        return repository.findAllById(iterable);
    }

    @Override
    public <S extends ExcelEntityTask> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public <S extends ExcelEntityTask> S saveAndFlush(S entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void deleteInBatch(Iterable<ExcelEntityTask> entities) {
        repository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public ExcelEntityTask getOne(String aLong) {
        return repository.getOne(aLong);
    }

    @Override
    public List<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec) {
        return repository.findAll(spec);
    }

    @Override
    public Page<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public List<ExcelEntityTask> findAll(Specification<ExcelEntityTask> spec, Sort sort) {
        return repository.findAll(spec, sort);
    }

    @Override
    public long count(Specification<ExcelEntityTask> spec) {
        return repository.count(spec);
    }


    @Override
    public <S extends ExcelEntityTask> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<ExcelEntityTask> findById(String aLong) {
        return repository.findById(aLong);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(String aLong) {
        repository.deleteById(aLong);
    }

    @Override
    public void delete(ExcelEntityTask entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends ExcelEntityTask> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean existsById(String aLong) {
        return repository.existsById(aLong);
    }
}
