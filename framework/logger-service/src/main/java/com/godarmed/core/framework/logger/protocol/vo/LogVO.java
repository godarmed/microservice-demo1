package com.godarmed.core.framework.logger.protocol.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;


@Data
public class LogVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String url;
	
	private String method;
	
	private String requestHeader;
	
	private String requestBody;
	
	private String status;
	
	private String exception;
	
	private String businessId;
	
	private String requestId;
	
	private String responseHeader;
	
	private String responseBody;
	
	private Long elapsedTime;
	
	private Timestamp responseTime;
}
