package com.godarmed.core.framework.logger.mysql;

import com.godarmed.core.framework.logger.core.LogSaveAction;
import com.godarmed.core.framework.logger.exception.LogSaveFailedException;
import com.godarmed.core.framework.logger.mysql.model.LoggerModel;
import com.godarmed.core.framework.logger.mysql.repository.LoggerRepository;
import com.godarmed.core.framework.logger.protocol.dto.LogDTO;
import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.global.utils.PropertiesUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(prefix="logger", name="persistence.drivers", havingValue="mysql", matchIfMissing=true)
@Primary
@Service
@Log4j2
public class LogSaveServiceImpl implements LogSaveAction {

	@Autowired
	LoggerRepository loggerRepository;
	

	@Override
	public void saveLog(HttpMessage message) throws LogSaveFailedException {
		// TODO Auto-generated method stub
		try {
			LoggerModel model = PropertiesUtils.transProperties(message, LoggerModel.class);
			if (model != null) {
				loggerRepository.save(model);
			}
		} catch (Exception e) {
			throw new LogSaveFailedException(e.getMessage());
		}
		
	}


	@Override
	public Page<LoggerModel> queryServMenuByPage(LogDTO logDTO) {
		Pageable pageable = PageRequest.of(logDTO.getPage(),logDTO.getSize(),Sort.by(Direction.DESC, "responseTime", "id"));
		Page<LoggerModel> servMenuPage = loggerRepository.findAll(new Specification<LoggerModel>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<LoggerModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if(logDTO.getUrl()!=null&&!"".equals(logDTO.getUrl())) {
					predicates.add(criteriaBuilder.like(root.get("url"), "%"+logDTO.getUrl()+"%"));
				}
				if(logDTO.getRequestId()!=null&&!"".equals(logDTO.getRequestId())) {
					predicates.add(criteriaBuilder.like(root.get("requestId"), "%"+logDTO.getRequestId()+"%"));
				}
				if(logDTO.getStatus()!=null&&!"".equals(logDTO.getStatus())) {
					predicates.add(criteriaBuilder.like(root.get("status"), "%"+logDTO.getStatus()+"%"));
				}
				if(logDTO.getRequestBody()!=null&&!"".equals(logDTO.getRequestBody())) {
					predicates.add(criteriaBuilder.like(root.get("requestBody"), "%"+logDTO.getRequestBody()+"%"));
				}
				if(logDTO.getResponseBody()!=null&&!"".equals(logDTO.getResponseBody())) {
					predicates.add(criteriaBuilder.like(root.get("responseBody"), "%"+logDTO.getResponseBody()+"%"));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		},pageable);
		return servMenuPage;
	}


	@Override
	public void delLogTask() {
		// TODO Auto-generated method stub
		LocalDateTime minusDays = LocalDateTime.now().minusDays(30);
		Timestamp timestamp = Timestamp.valueOf(minusDays);
		List<LoggerModel> delLoggerModels = loggerRepository.findByResponseTimeBefore(timestamp);
		if (delLoggerModels != null && delLoggerModels.size() > 0) {
			loggerRepository.deleteAll(delLoggerModels);
			log.info("删除一月之前的日志记录已完成，共{}条",delLoggerModels.size());
		}else {
			log.info("没有找到一月之前的日志记录");
		}
	}

}
