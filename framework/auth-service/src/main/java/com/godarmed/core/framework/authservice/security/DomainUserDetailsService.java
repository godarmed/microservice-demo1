package com.godarmed.core.framework.authservice.security;


import com.godarmed.core.framework.authservice.module.model.ServUserInfo;
import com.godarmed.core.framework.authservice.module.service.ServUserInfoService;
import com.godarmed.core.starters.authserver.interfaces.UserService;
import com.godarmed.core.starters.global.entity.UserViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户信息服务
 * 实现 Spring Security的UserDetailsService接口方法，用于身份认证
 */
@Service
public class DomainUserDetailsService implements UserService {

    @Autowired
    private ServUserInfoService servUserInfoService;    // 账户数据操作接口

	@Override
	public UserViews getUserByName(String name) {
		// TODO Auto-generated method stub
		if (name != null) {
			ServUserInfo account = servUserInfoService.findByUserName(name);
			if (account != null) {
				UserViews userViews = new UserViews();
				userViews.setId(account.getId());
				userViews.setName(name);
				userViews.setPassword(account.getPassWord());
				return userViews;
			}
		}
		return null;
	}
}
