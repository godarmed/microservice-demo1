package com.godarmed.core.framework.authservice.UserTools.entity;

import lombok.Data;

@Data
public class ExecuteSQLEntity {
	private String secretKey;
	
	private String timeType;
	
	private String sql; 
	
	private String jdbcUrl;
	
	private String dbUser;
	
	private String password;
}
