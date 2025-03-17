package com.zq.backend.repository.impl;

import com.alibaba.fastjson2.JSON;
import com.zq.backend.converter.DOConverter;
import com.zq.backend.converter.DTOConverter;
import com.zq.backend.dao.UserDAO;
import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.object.params.ListUserParam;
import com.zq.backend.repository.UserRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Resource
    private UserDAO userDAO;

    private static final String CACHE_NAME = "userCache";

    @Override
    @CacheEvict(value=CACHE_NAME, key="#userDTO.username")
    public void create(UserDTOWithPassword userDTO) {
        ParamChecker.checkNotNull(userDTO, "userDTO");
        if(StringUtils.isBlank(userDTO.getNick())) {
            userDTO.setNick(userDTO.getUsername());
        }
        if(Objects.isNull(userDTO.getExtension())) {
            userDTO.setExtension(new UserExtension());
        }

        UserDO existingUser = getUserDO(userDTO.getUsername());
        if (Objects.nonNull(existingUser)) {
            ExceptionUtil.throwException(ErrorEnum.USER_EXISTS);
        }

        UserDO userDO = DOConverter.INSTANCE.toUserDO(userDTO);
        userDAO.insertUser(userDO);
    }

    @Override
    public UserDTO getByUserName(String username) {
        UserDO userDO = getUserDO(username);
        return DTOConverter.INSTANCE.toUserDTO(userDO);
    }

    @Override
    public UserDTOWithPassword getWithPasswordByUserName(String username) {
        UserDO userDO = getUserDO(username);
        return DTOConverter.INSTANCE.toUserDTOWithPassword(userDO);
    }

    @Override
    @CacheEvict(value=CACHE_NAME, key="#username")
    public int updateExtension(String username, UserExtension userExtension) {
        ParamChecker.checkNotBlank(username, "username");
        return userDAO.updateExtension(username, JSON.toJSONString(userExtension));
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#userDO.username")
    public int updateUser(UserDO userDO) {
        ParamChecker.checkNotNull(userDO, "userDO");
        return userDAO.updateUser(userDO);
    }

    @Override
    @CacheEvict(value=CACHE_NAME, key="#username")
    public int updatePassword(String username, String encodedPassword) {
        ParamChecker.checkNotBlank(username, "username");
        ParamChecker.checkNotBlank(encodedPassword, "password");
        return userDAO.updatePassword(username, encodedPassword);
    }

    @Override
    public List<UserDTO> listUser(ListUserParam param) {
        ParamChecker.checkNotNull(param, "param");
        return DTOConverter.INSTANCE.toUserDTOList(userDAO.listUser(param));
    }

    @Override
    public int updateRole(String username, RoleTypeEnum roleTypeEnum) {
        ParamChecker.checkNotBlank(username, "username");
        ParamChecker.checkNotNull(roleTypeEnum, "roleType");
        return userDAO.updateRole(username, roleTypeEnum.getRoleName());
    }

    @Cacheable(value=CACHE_NAME, key="#username")
    public UserDO getUserDO(String username) {
        ParamChecker.checkNotBlank(username, "username");
        return userDAO.getByUserName(username);
    }
}
