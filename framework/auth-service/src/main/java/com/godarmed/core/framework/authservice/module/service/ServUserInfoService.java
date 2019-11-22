package com.godarmed.core.framework.authservice.module.service;


import com.godarmed.core.framework.authservice.module.model.ServUserInfo;
import com.godarmed.core.framework.authservice.protocol.dto.ServUserInfoDTO;
import com.godarmed.core.framework.authservice.protocol.vo.ServUserInfoVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ServUserInfoService {
	ServUserInfo findByUserName(String userName);
	
	ServUserInfoVO addUserInfo(ServUserInfoDTO servUserInfoDTO);

	Page<ServUserInfo> queryUserInfo(ServUserInfoDTO servUserInfoDTO);

    ServUserInfoVO findById(ServUserInfoDTO servUserInfoDTO);

	ServUserInfoVO updateServUserInfo(ServUserInfoDTO servUserInfoDTO);

	ServUserInfoVO deleteServUserInfoById(ServUserInfoDTO servUserInfoDTO);

	ServUserInfoVO updatePwd(ServUserInfoDTO servUserInfoDTO);
	
	boolean CheckUsername(ServUserInfoDTO servUserInfoDTO);
	
	ServUserInfoVO forceOffLine(ServUserInfoDTO servUserInfoDTO);
}
