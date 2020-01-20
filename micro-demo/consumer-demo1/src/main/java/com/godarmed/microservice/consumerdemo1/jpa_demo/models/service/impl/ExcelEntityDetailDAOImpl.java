package com.godarmed.microservice.consumerdemo1.jpa_demo.models.service.impl;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityDetailRepository;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.service.ExcelEntityDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelEntityDetailDAOImpl implements ExcelEntityDetailDAO{

    @Autowired
    ExcelEntityDetailRepository repository;

    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL_LIST"},key = "List<ExcelEntityDetail>findAll",unless = "#result == null")
    @Override
    public List<ExcelEntityDetail> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL_LIST"},key = "'List<ExcelEntityDetail>findAll-sort'+#sort",unless = "#result == null")
    @Override
    public List<ExcelEntityDetail> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public List<ExcelEntityDetail> findAllById(Iterable<Long> iterable) {
        return repository.findAllById(iterable);
    }

    @CacheEvict(value = {"SERV_EXCELENTITY_DETAIL_LIST"}) // 清空批量查询
    @Override
    public <S extends ExcelEntityDetail> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public <S extends ExcelEntityDetail> S saveAndFlush(S entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void deleteInBatch(Iterable<ExcelEntityDetail> entities) {
        repository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public ExcelEntityDetail getOne(Long aLong) {
        return repository.getOne(aLong);
    }

    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL_LIST"},key = "'List<ExcelEntityDetail>-spec'+#spec",unless = "#result == null")
    @Override
    public List<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec) {
        return repository.findAll(spec);
    }

    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL_LIST"},key = "'Page<ExcelEntityDetail>-spec'+#spec",unless = "#result == null")
    @Override
    public Page<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public List<ExcelEntityDetail> findAll(Specification<ExcelEntityDetail> spec, Sort sort) {
        return repository.findAll(spec, sort);
    }

    @Override
    public long count(Specification<ExcelEntityDetail> spec) {
        return repository.count(spec);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = {"SERV_EXCELENTITY_DETAIL_LIST"}) // 清空批量查询
            },
            put = {
                    @CachePut(value = {"SERV_EXCELENTITY_DETAIL"},key="'ExcelEntityDetail-id'+#entity.id") // 设置单个查询
            }
    )
    @Override
    public <S extends ExcelEntityDetail> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<ExcelEntityDetail> findById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = {"SERV_EXCELENTITY_DETAIL_LIST"}), // 清空批量查询
                    @CacheEvict(value = {"SERV_EXCELENTITY_DETAIL"},key="'ExcelEntityDetail-id'+#entity.id")
            }
    )
    @Override
    public void delete(ExcelEntityDetail entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends ExcelEntityDetail> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }


    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL"},key="'ExcelEntityDetail'+#id",unless = "#result == null") // 根据id查询user 以另一种key将查询出的结果缓存到缓存中
    @Override
    public ExcelEntityDetail findExcelEntityById(Long id) {
        return repository.findExcelEntityDetailById(id);
    }

    @Cacheable(value = {"SERV_EXCELENTITY_DETAIL_LIST"},key="'List<ExcelEntityDetail>-taskId'+#taskId",unless = "#result == null") // 根据id查询user 以另一种key将查询出的结果缓存到缓存中
    @Override
    public List<ExcelEntityDetail> findExcelEntityByTaskId(Long taskId) {
        return repository.findExcelEntityDetailByTaskId(taskId);
    }
}
