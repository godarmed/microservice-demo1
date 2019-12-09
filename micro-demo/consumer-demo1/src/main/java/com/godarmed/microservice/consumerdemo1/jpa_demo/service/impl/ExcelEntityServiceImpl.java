package com.godarmed.microservice.consumerdemo1.jpa_demo.service.impl;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityDetailRepository;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityTaskRepository;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.BaseExcelEntityDTO;
import com.godarmed.microservice.consumerdemo1.jpa_demo.service.ExcelEntityService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ExcelEntityServiceImpl implements ExcelEntityService {

    @Autowired
    ExcelEntityDetailRepository excelEntityDetailRepository;

    @Autowired
    ExcelEntityTaskRepository excelEntityTaskRepository;

    @Override
    public ExcelEntityTask saveExcelEntityTask(ExcelEntityTask excelEntityTask) {
        // TODO Auto-generated method stub
        return excelEntityTaskRepository.save(excelEntityTask);
    }

    @Override
    public ExcelEntityDetail saveExcelEntityDetail(ExcelEntityDetail excelEntityDetail) {
        // TODO Auto-generated method stub
        return excelEntityDetailRepository.save(excelEntityDetail);
    }

    @Override
    public ExcelEntityTask queryExcelEntityTaskById(String id) {
        return excelEntityTaskRepository.findById(id).get();
    }

    @Override
    public ExcelEntityDetail queryExcelEntityDetailById(Long id) {
        return excelEntityDetailRepository.findById(id).get();
    }

    @Override
    public Page<ExcelEntityTask> queryExcelEntityTask(BaseExcelEntityDTO baseExcelEntityDTO) {
        Pageable pageable = PageRequest.of(baseExcelEntityDTO.getPage(), baseExcelEntityDTO.getSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<ExcelEntityTask> tpProductVOPage = excelEntityTaskRepository.findAll(new Specification<ExcelEntityTask>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ExcelEntityTask> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (baseExcelEntityDTO.getId() != null && !"".equals(baseExcelEntityDTO.getId())) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), baseExcelEntityDTO.getId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return tpProductVOPage;
    }

    @Override
    public List<ExcelEntityTask> queryExcelEntityTaskAll() {
        return excelEntityTaskRepository.findAll();
    }

    @Override
    public String deleteExcelEntityTask(String id) {
        //查询
        if (excelEntityTaskRepository.existsById(id)) {
            excelEntityTaskRepository.deleteById(id);
        }
        return id;
    }

    @Override
    public Long deleteExcelEntityDetail(long id) {
        //查询
        if (excelEntityDetailRepository.existsById(id)) {
            excelEntityDetailRepository.deleteById(id);
        }
        return id;
    }
}
