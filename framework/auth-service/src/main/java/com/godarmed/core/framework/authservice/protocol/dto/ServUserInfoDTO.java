package com.godarmed.core.framework.authservice.protocol.dto;

import lombok.Data;

@Data
public class ServUserInfoDTO {
	  private Long id;
	    private String userName;
	    private String passWord;
	    private String mobile;
	    private String state;
		
		private Integer page=0;
		private Integer size=10;

}
