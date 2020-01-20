package com.godarmed.microservice.consumerdemo1.jpa_demo.service;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.ExcelEntityDetailDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExcelEntityDetailService {

    ExcelEntityDetail add(ExcelEntityDetailDTO request);

    ExcelEntityDetail update(ExcelEntityDetailDTO request);

    ExcelEntityDetail deleteById(Long id);

    List<ExcelEntityDetail> deleteByIds(List<Long> ids);

    Page<ExcelEntityDetail> queryByPage(ExcelEntityDetailDTO request);

    List<ExcelEntityDetail> queryAll(ExcelEntityDetailDTO request);

    ExcelEntityDetail queryById(Long id);


}
