package com.godarmed.microservice.defaultdemo.querydslDemo.controller;

import com.alibaba.fastjson.JSON;
import com.godarmed.microservice.defaultdemo.querydslDemo.DAO.repository.TcityRepository;
import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Tcity;
import com.godarmed.microservice.defaultdemo.querydslDemo.model.protocol.DTO.TcityDTO;
import com.godarmed.microservice.defaultdemo.querydslDemo.model.protocol.VO.TcityVO;
import com.godarmed.microservice.defaultdemo.querydslDemo.model.queryEntity.QTcity;
import com.godarmed.microservice.defaultdemo.querydslDemo.model.queryEntity.QThotel;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname QueryDslController
 * @Description TODO
 * @Date 2020/4/21 10:09
 * @Created by Administrator
 */
@Slf4j
@RestController
public class QueryDslController {

    @Autowired
    private TcityRepository tcityRepository;

    @PersistenceContext
    private EntityManager em;

    @PostMapping("/queryTest/querySingleTable")
    public Page<Tcity> querySingleTable() {
        //查找出Id小于3,并且名称带有`shanghai`的记录.
        //动态条件
        QTcity qtCity = QTcity.tcity;
        //该Predicate为querydsl下的类,支持嵌套组装复杂查询条件
        Predicate predicate = qtCity.id.longValue().lt(3)
                .and(qtCity.name.like("shanghai"));
        //分页排序
        //Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest = PageRequest.of(0, 10);
        //查找结果
        Page<Tcity> tCityPage = tcityRepository.findAll(predicate, pageRequest);
        return tCityPage;
    }

    @PostMapping("/queryTest/queryManyTables")
    public List<Tuple> queryManyTables(@RequestBody TcityDTO tcityDTO) {
        QTcity qtCity = QTcity.tcity;
        QThotel qtHotel = QThotel.thotel;
        //条件
        List<Predicate> predicates = new ArrayList<>();
        if (Strings.isNotBlank(tcityDTO.getName())) {
            predicates.add(qtCity.name.like(tcityDTO.getName()));
        }
        //分页
        PageRequest pageRequest = PageRequest.of(tcityDTO.getPage(), tcityDTO.getSize());
        //调用查询
        List<Tuple> result = findCityAndHotel(predicates, pageRequest);
        //结果取出
        List<TcityVO> tcityVOS = result.stream().map(item -> {
            TcityVO temp = new TcityVO();
            BeanUtils.copyProperties(item, temp);
            return temp;
        }).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(tcityVOS));
        //取出count查询总数
        System.out.println(result.size());
        return result;
    }


    /**
     * 关联查询示例,查询出城市和对应的旅店
     *
     * @param predicates 查询条件
     * @return 查询实体
     */
    public List<Tcity> findCityAndHotel(List<Predicate> predicates) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        //子查询条件
        QTcity tc = new QTcity("tc");
        QThotel th = new QThotel("th");
        Predicate subQuery = QTcity.tcity.id.in(
                JPAExpressions.select(tc.id)
                        .from(tc)
                        .leftJoin(th)
                        .on(th.city.longValue().eq(tc.id.longValue()))
                        .where(predicates.toArray(new Predicate[predicates.size()]))
                        .orderBy(tc.name.asc())
                        .offset(0)
                        .limit(1)

        );

        //添加查询条件
        List<Tcity> tcities = queryFactory.select(QTcity.tcity)
                .from(QTcity.tcity)
                .leftJoin(QThotel.thotel)
                .on(QThotel.thotel.city.longValue().eq(QTcity.tcity.id.longValue()))
                .where(subQuery)
                .fetch();

        //拿到结果
        return tcities;
    }

    /**
     * 关联查询示例,查询出城市和对应的旅店
     *
     * @param predicates 查询条件
     * @return 查询实体
     */
    public List<Tuple> findCityAndHotel(List<Predicate> predicates, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        //子查询条件
        QTcity tc = new QTcity("tc");
        QThotel th = new QThotel("th");
        Predicate subQuery = QTcity.tcity.id.in(
                JPAExpressions.select(tc.id)
                        .from(tc)
                        .leftJoin(th)
                        .on(th.city.longValue().eq(tc.id.longValue()))
                        .distinct()
                        .where(predicates.toArray(new Predicate[predicates.size()]))
                        .orderBy(th.address.desc())

        );

        //添加查询条件
        List<Tuple> tcities = queryFactory.select(QTcity.tcity, QThotel.thotel)
                .from(QTcity.tcity)
                .leftJoin(QThotel.thotel)
                .on(QThotel.thotel.city.longValue().eq(QTcity.tcity.id.longValue()))
                .where(subQuery)
                .orderBy(QThotel.thotel.address.desc())
                .fetch();
        //拿到结果
        return tcities;
    }


}
