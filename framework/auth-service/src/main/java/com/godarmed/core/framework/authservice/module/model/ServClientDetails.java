package com.godarmed.core.framework.authservice.module.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "serv_client_details", indexes= {
		@Index(name="serv_client_details_clientId", columnList="clientId")
})
public class ServClientDetails implements Serializable, ClientDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String clientId;
	
	private String resourceIds;
	
	private String secret;
	
	private String scope;
	
	private String grantTypes;
	
	private String redirect;
	
	private String authorities;
	
	private Integer accessTokenValidity;
	
	private Integer refreshTokenValidity;
	
	@Column(length=4096)
	private String additionalInformation;
	
	private String autoApproveScopes;

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return string2Set(resourceIds);
	}

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getClientSecret() {
		// TODO Auto-generated method stub
		return secret;
	}

	@Override
	public boolean isScoped() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return string2Set(scope);
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return string2Set(grantTypes);
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return string2Set(redirect);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<String> auths = string2Set(authorities);
		if (auths != null) {
			return auths.stream().map(item -> {
				return new SimpleGrantedAuthority(item);
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return accessTokenValidity;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return refreshTokenValidity;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Set<String> string2Set(String data) {
		if (data != null) {
			Set<String> result = new HashSet<>();
			String[] ids = data.split(",");
			for (String id: ids) {
				result.add(id);
			}
			return result;
		}
		return null;
	}  

}
/**
 * 
 * create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);
 * 
 * 
 * 
 * 
 */