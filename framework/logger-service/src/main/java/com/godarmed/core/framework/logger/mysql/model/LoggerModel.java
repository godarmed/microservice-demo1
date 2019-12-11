package com.godarmed.core.framework.logger.mysql.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="log_record", indexes= {
		@Index(name="log_record_bussiness", columnList = "business_id"),
		@Index(name="log_record_reqeust", columnList = "request_id")
})
public class LoggerModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="url", nullable=false)
	private String url;
	
	@Column(name="`method`", nullable=false)
	private String method;
	
	@Column(name="`request_header`", columnDefinition="text")
	private String requestHeader;
	
	@Column(name="`reqeust_body`", columnDefinition="text")
	private String requestBody;
	
	@Column(name="`status`", nullable=false)
	private String status;
	
	@Column(name="`exception`", columnDefinition="text")
	private String exception;
	
	@Column(name="`business_id`")
	private String businessId;
	
	@Column(name="`request_id`", nullable=false)
	private String requestId;
	
	@Column(name="`response_header`", columnDefinition="text")
	private String responseHeader;
	
	@Column(name="`response_body`", columnDefinition="text")
	private String responseBody;
	
	private Long elapsedTime;
	
	private Timestamp responseTime;
}
