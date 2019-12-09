package com.godarmed.microservice.consumerdemo1.entityManager_demo.controller;

import com.godarmed.microservice.consumerdemo1.common.timeLog.annotation.Log;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity.ExcelEntityTask;
import com.godarmed.microservice.consumerdemo1.jpa_demo.models.repository.ExcelEntityTaskRepository;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/entityManagerTest")
public class EntityManagerTestController {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    ExcelEntityTaskRepository repository;

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    @RequestMapping("/create")
    @Log
    public boolean ordionarySave(Integer num) {

        List<ExcelEntityTask> targetList = getEntityList(num);
        repository.saveAll(targetList);
        return true;
    }

    @RequestMapping("/createBatch")
    @Log
    @Transactional(rollbackFor = {Exception.class})
    public boolean batchSave(Integer num) {

        List<ExcelEntityTask> targetList = getEntityList(num);

        for (ExcelEntityTask table : targetList){
            /** 将要批量插入的实例转换为managed(托管)状态 */
            em.persist(table);
        }
        /** 提交事务，实例将会被插入到数据库中 */
        em.flush();
        em.clear();
        return true;
    }

    @RequestMapping("/update")
    @Log
    public boolean ordionaryUpdate(Integer num) {

        List<ExcelEntityTask> targetList = repository.findAll();
        for (ExcelEntityTask excelEntityTask : targetList) {
            ExcelEntityTask newExcel = repository.findById(excelEntityTask.getId()).get();
            excelEntityTask.setTaskName("update");
            excelEntityTask.setTotal(excelEntityTask.getTotal()+1);
            repository.save(excelEntityTask);
        }
        repository.saveAll(targetList);
        return true;
    }

    @RequestMapping("/updateBatch")
    @Log
    @Transactional(rollbackFor = {Exception.class})
    public boolean batchUpdate(Integer num) {

        List<ExcelEntityTask> targetList = repository.findAll();

        for (ExcelEntityTask table : targetList){
            /** 将要批量插入的实例转换为managed(托管)状态 */
            ExcelEntityTask newExcel = repository.findById(table.getId()).get();
            newExcel.setTaskName("update");
            newExcel.setTotal(table.getTotal()+1);
            em.merge(newExcel);
        }
        /** 提交事务，实例将会被插入到数据库中 */
        em.flush();
        em.clear();
        return true;
    }


    @RequestMapping("/clearAll")
    @Log
    public boolean clearAll(Integer num) {
        repository.deleteAllInBatch();
        return true;
    }

    public List<ExcelEntityTask> getEntityList(Integer num){
        List<ExcelEntityTask> targetList = new ArrayList<>();
        ExcelEntityTask source = new ExcelEntityTask();
        source.setCreateName("leo");
        source.setTaskName("entity_manager_test");
        source.setTotal(threadLocalRandom.nextInt(0,Integer.MAX_VALUE));
        //source.setUpdateTime(new Date());

        for (int i = 0; i < num; i++) {
            ExcelEntityTask target = new ExcelEntityTask();
            BeanUtils.copyProperties(source,target);
            target.setTotal(threadLocalRandom.nextInt(0,Integer.MAX_VALUE));
            targetList.add(target);
        }

        return  targetList;
    }

}
