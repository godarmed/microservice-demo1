package com.godarmed.core.framework.logger.protocol.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LogDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	
	private String requestBody;
	
	private String responseBody;
	
	private String status;
	
	private String businessId;
	
	private String requestId;
	
	private Integer page=0;
	
	private Integer size=10;
	
}
