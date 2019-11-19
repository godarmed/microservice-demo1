package com.godarmed.microservice.consumerdemo1.jpa_demo.service;

import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityDetail;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.BaseExcelEntityDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExcelEntityService {
    ExcelEntityTask saveExcelEntityTask(ExcelEntityTask excelEntityTask);

    ExcelEntityDetail saveExcelEntityDetail(ExcelEntityDetail excelEntityDetail);

    List<ExcelEntityTask> queryExcelEntityTaskAll();

    Page<ExcelEntityTask> queryExcelEntityTask(BaseExcelEntityDTO baseExcelEntityDTO);

    ExcelEntityTask queryExcelEntityTaskById(Long id);

    ExcelEntityDetail queryExcelEntityDetailById(Long id);

    Long deleteExcelEntityTask(long id);

    Long deleteExcelEntityDetail(long id);


}
