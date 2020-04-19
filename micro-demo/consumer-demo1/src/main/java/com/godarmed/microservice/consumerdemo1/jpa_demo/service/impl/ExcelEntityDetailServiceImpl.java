package com.godarmed.microservice.consumerdemo1.jpa_demo.service.impl;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.service.ExcelEntityDetailDAO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.ExcelEntityDetailDTO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.service.ExcelEntityDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExcelEntityDetailServiceImpl implements ExcelEntityDetailService {

    ExcelUtil excelUtil;

    BigExcelWriter writer;

    @Autowired
    ExcelEntityDetailDAO excelEntityDetailDAO;

    @Override
    public ExcelEntityDetail add(ExcelEntityDetailDTO request) {
        ExcelEntityDetail excelEntityDetail = new ExcelEntityDetail();
        BeanUtils.copyProperties(request, excelEntityDetail);
        return excelEntityDetailDAO.save(excelEntityDetail);
    }

    @Override
    public ExcelEntityDetail update(ExcelEntityDetailDTO request) {
        ExcelEntityDetail oldDetail = excelEntityDetailDAO.findExcelEntityById(request.getId());
        ExcelEntityDetail newDetail = null;
        if (oldDetail != null) {
            newDetail = new ExcelEntityDetail();
            BeanUtils.copyProperties(request, newDetail);
            newDetail = excelEntityDetailDAO.save(newDetail);
        } else {
            throw new RuntimeException("任务详情不存在");
        }

        return newDetail;
    }

    @Override
    public ExcelEntityDetail deleteById(Long id) {
        ExcelEntityDetail excelEntityDetail = excelEntityDetailDAO.findExcelEntityById(id);
        if (excelEntityDetail != null) {
            excelEntityDetailDAO.delete(excelEntityDetail);
        } else {
            throw new RuntimeException("任务详情不存在");
        }
        return excelEntityDetail;
    }

    @Override
    public List<ExcelEntityDetail> deleteByIds(List<Long> ids) {
        List<ExcelEntityDetail> excelEntityDetails = excelEntityDetailDAO.findAllById(ids);
        if (excelEntityDetails != null && excelEntityDetails.size() > 0) {
            excelEntityDetailDAO.deleteAll(excelEntityDetails);
        } else {
            throw new RuntimeException("任务详情不存在");
        }
        return excelEntityDetails;
    }

    @Override
    public Page<ExcelEntityDetail> queryByPage(ExcelEntityDetailDTO request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<ExcelEntityDetail> pageList = excelEntityDetailDAO.findAll(new Specification<ExcelEntityDetail>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ExcelEntityDetail> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (request.getId() != null && !"".equals(request.getId())) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return pageList;
    }

    @Override
    public ExcelEntityDetail queryById(Long id) {
        return excelEntityDetailDAO.findExcelEntityById(id);
    }

    @Override
    public List<ExcelEntityDetail> queryAll(ExcelEntityDetailDTO request) {
        return null;
    }
}
