package com.godarmed.core.framework.authservice.module.service.impl;

import com.godarmed.core.framework.authservice.BusiException.BusiEnum;
import com.godarmed.core.framework.authservice.BusiException.BusiException;
import com.godarmed.core.framework.authservice.module.model.AuthAccessToken;
import com.godarmed.core.framework.authservice.module.model.ServUserInfo;
import com.godarmed.core.framework.authservice.module.repository.AuthAccessTokenRepository;
import com.godarmed.core.framework.authservice.module.repository.ServUserInfoRepository;
import com.godarmed.core.framework.authservice.module.service.ServUserInfoService;
import com.godarmed.core.framework.authservice.protocol.dto.ServUserInfoDTO;
import com.godarmed.core.framework.authservice.protocol.vo.ServUserInfoVO;
import com.godarmed.core.starters.global.entity.ReflexEntity;
import com.godarmed.core.starters.global.utils.ReflexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.godarmed.core.framework.authservice.BusiException.BusiEnum.NOT_FOUND_USER;


@Service
public class ServUserInfoServiceImpl implements ServUserInfoService {

    @Autowired
    ServUserInfoRepository servUserInfoRepository;
    @Autowired
    AuthAccessTokenRepository authAccessTokenRepository;

    private List<Predicate> predicates;
    private List<String> excludes = Arrays.asList(new String[]{"page", "size"});

    @Override
    public ServUserInfo findByUserName(String userName) {
        // TODO Auto-generated method stub
        Optional<ServUserInfo> op = servUserInfoRepository.findByUserName(userName);
        if (op.isPresent()) {
            ServUserInfo userInfo = op.get();
            return userInfo;
        }
        return null;
    }

    @Override
    public ServUserInfoVO updateServUserInfo(ServUserInfoDTO servUserInfoDTO) {
        // TODO Auto-generated method stub
        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        if (servUserInfoDTO.getId() != null) {

            Optional<ServUserInfo> optional = servUserInfoRepository.findById(servUserInfoDTO.getId());
            if (optional.isPresent()) {
                if (!CheckUsername(servUserInfoDTO)) {
                    BeanUtils.copyProperties(optional.get(), servUserInfoVO);
                    ServUserInfo servUserInfo = optional.get();
                    BeanUtils.copyProperties(servUserInfoDTO, servUserInfo);
                    servUserInfo = servUserInfoRepository.save(servUserInfo);
                    BeanUtils.copyProperties(servUserInfo, servUserInfoVO);
                } else {
                    if (servUserInfoDTO.getUserName().equals(optional.get().getUserName())) {
                        BeanUtils.copyProperties(optional.get(), servUserInfoVO);
                        ServUserInfo servUserInfo = optional.get();
                        BeanUtils.copyProperties(servUserInfoDTO, servUserInfo);
                        servUserInfo = servUserInfoRepository.save(servUserInfo);
                        BeanUtils.copyProperties(servUserInfo, servUserInfoVO);
                    } else {
                        throw new BusiException(BusiEnum.USERNAME_REPEATABLE);
                    }
                }

            } else {
                throw new BusiException(NOT_FOUND_USER);
            }

        } else {
            throw new BusiException(BusiEnum.USERINFO_NOID);
        }
        return servUserInfoVO;
    }

    @Override
    public ServUserInfoVO deleteServUserInfoById(ServUserInfoDTO servUserInfoDTO) {
        // TODO Auto-generated method stub
        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        if (servUserInfoDTO.getId() != null) {

            Optional<ServUserInfo> userInfo = servUserInfoRepository.findById(servUserInfoDTO.getId());
            if (userInfo.isPresent()) {
                servUserInfoRepository.deleteById(userInfo.get().getId());
                BeanUtils.copyProperties(userInfo.get(), servUserInfoVO);
            } else {
                throw new BusiException(BusiEnum.NOT_FOUND_USER);
            }
        } else {
            throw new BusiException(BusiEnum.USERINFO_IDNOTNULL);
        }
        return servUserInfoVO;
    }

    @Override
    @Transactional
    public ServUserInfoVO addUserInfo(ServUserInfoDTO servUserInfoDTO) {
        ServUserInfo servUserInfo = new ServUserInfo();
        BeanUtils.copyProperties(servUserInfoDTO, servUserInfo);

        ckeckUserName(servUserInfo);

        ServUserInfo savedServUserInfo = new ServUserInfo();
        if (servUserInfo.getId() == null) {
            savedServUserInfo = servUserInfoRepository.save(servUserInfo);
        } else {
            throw new BusiException(BusiEnum.USERINFO_NOID);
        }
        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        BeanUtils.copyProperties(savedServUserInfo, servUserInfoVO);
        // TODO Auto-generated method stub
        return servUserInfoVO;
    }

    private void ckeckUserName(ServUserInfo servUserInfo) {

        if (servUserInfoRepository.findByUserName(servUserInfo.getUserName()).isPresent()) {
            throw new BusiException(BusiEnum.USERNAME_REPEATABLE);
        }
    }


    @Override
    public Page<ServUserInfo> queryUserInfo(ServUserInfoDTO servUserInfoDTO) {
        // TODO Auto-generated method stub

        Iterable<ServUserInfo> allUserInfo = servUserInfoRepository.findAll();
        for (ServUserInfo item : allUserInfo) {
            List<AuthAccessToken> AuthUser = authAccessTokenRepository.findByUserName(item.getUserName());
            if (AuthUser.size() == 0 || item.getState() == null) {
                item.setState("0");
            } else
                item.setState("1");
            servUserInfoRepository.save(item);
        }

        Pageable pageable = PageRequest.of(servUserInfoDTO.getPage(), servUserInfoDTO.getSize(), Sort.by(Direction.DESC, "id"));
        return servUserInfoRepository.findAll(new Specification<ServUserInfo>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ServUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // TODO Auto-generated method stub
                try {
                    query(root, query, criteriaBuilder, servUserInfoDTO);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
    }

    private void query(Root<ServUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, ServUserInfoDTO servUserInfoDTO) throws IllegalAccessException {
        predicates = new ArrayList<>();
        List<ReflexEntity> enties = null;
        enties = ReflexUtils.reflex2EntityAndSuperClass(servUserInfoDTO);

        if (enties != null && enties.size() > 0) {

            for (ReflexEntity entity : enties) {
                Class<?> type = entity.getType();
                String fieldName = entity.getName();
                Object value = entity.getValue();
                if (excludes.get(0).contains(fieldName))
                    break;
                if (String.class.isAssignableFrom(type) && value != null && !value.equals(""))
                    predicates.add(criteriaBuilder.like(root.get(fieldName), "%" + String.valueOf(value) + "%"));
                if (Long.class.isAssignableFrom(type) && value != null)
                    predicates.add(criteriaBuilder.equal(root.get(fieldName), (Long) value));

            }
        }
    }


    @Override
    public ServUserInfoVO findById(ServUserInfoDTO servUserInfoDTO) {
        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        ServUserInfo servUserInfo = new ServUserInfo();
        BeanUtils.copyProperties(servUserInfoDTO, servUserInfo);
        if (servUserInfo.getId() != null) {

            Optional<ServUserInfo> userInfo = servUserInfoRepository.findById(servUserInfo.getId());
            if (userInfo.isPresent() && userInfo.get() != null) {
                BeanUtils.copyProperties(userInfo.get(), servUserInfoVO);
                List<AuthAccessToken> authAccessToken = authAccessTokenRepository.findByUserName(userInfo.get().getUserName());
                if (authAccessToken == null)
                    servUserInfoVO.setState("0");
                else
                    servUserInfoVO.setState("1");
            }
        }
        return servUserInfoVO;
    }


    @Override
    public ServUserInfoVO updatePwd(ServUserInfoDTO servUserInfoDTO) {
        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        ServUserInfo servUserInfo = new ServUserInfo();
        BeanUtils.copyProperties(servUserInfoDTO, servUserInfo);
        if (servUserInfo.getId() != null && servUserInfo.getPassWord() != null) {
            Optional<ServUserInfo> userInfo = servUserInfoRepository.findById(servUserInfo.getId());
            ServUserInfo saveInfo;
            if (userInfo.isPresent() && (saveInfo = userInfo.get()) != null) {
                saveInfo.setPassWord(servUserInfo.getPassWord());
                try {
                    servUserInfoRepository.save(saveInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BeanUtils.copyProperties(saveInfo, servUserInfoVO);
            }
        }

        return servUserInfoVO;
    }

    @Override
    public boolean CheckUsername(ServUserInfoDTO servUserInfoDTO) {
        // TODO Auto-generated method stub
        Optional<ServUserInfo> ServUserInfo = servUserInfoRepository.findByUserName(servUserInfoDTO.getUserName());
        if (ServUserInfo.isPresent())
            return true;
        else
            return false;
    }

    @Override
    public ServUserInfoVO forceOffLine(ServUserInfoDTO servUserInfoDTO) {

        ServUserInfoVO servUserInfoVO = new ServUserInfoVO();
        if (servUserInfoDTO.getUserName() != null && !"".equals(servUserInfoDTO.getUserName())) {

            List<AuthAccessToken> authUser = authAccessTokenRepository.findByUserName(servUserInfoDTO.getUserName());
            if (authUser.size() == 0)
                throw new BusiException(BusiEnum.USER_INVALID);
            else if (authUser.size() >= 1) {
                for (int i = 0; i < authUser.size(); i++) {
                    authAccessTokenRepository.deleteById(authUser.get(i).getTokenId());
                }
            } else
                throw new BusiException(BusiEnum.NOTDELETE);

            Optional<ServUserInfo> userInfo = servUserInfoRepository.findByUserName(servUserInfoDTO.getUserName());
            if (userInfo.isPresent()) {
                ServUserInfo info = userInfo.get();
                info.setState("0");
                servUserInfoRepository.save(info);
            }

            BeanUtils.copyProperties(userInfo.get(), servUserInfoVO);
        } else {
            throw new BusiException(BusiEnum.USERINFO_IDNOTNULL);
        }
        return servUserInfoVO;
    }

}
