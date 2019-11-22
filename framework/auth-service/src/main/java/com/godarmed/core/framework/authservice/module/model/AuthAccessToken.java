package com.godarmed.core.framework.authservice.module.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "oauth_access_token", indexes= {
		@Index(name="token_id_index", columnList="tokenId"),
		@Index(name="authentication_id_index", columnList="authenticationId"),
		@Index(name="user_name_index", columnList="userName"),
		@Index(name="client_id_index", columnList="clientId"),
		@Index(name="refresh_token_index", columnList="refreshToken")
})
public class AuthAccessToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String tokenId;
	
	private Timestamp createTime;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] token;
	
	private String authenticationId;
	
	private String userName;
	
	private String clientId;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] authentication;
	
	private String refreshToken;
}
