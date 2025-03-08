package com.zq.backend.repository;

import com.zq.backend.converter.DOConverter;
import com.zq.backend.converter.DTOConverter;
import com.zq.backend.dao.UserDao;
import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Resource
    private UserDao userDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    public void create(UserDTO userDTO) {
        ParamChecker.checkNotNull(userDTO, "userDTO");
        ParamChecker.checkNotBlank(userDTO.getUserName(), "userName");
        ParamChecker.checkNotBlank(userDTO.getPassword(),  "password");

        UserDO existingUser = userDao.getByUserName(userDTO.getUserName());
        if (existingUser != null) {
            ExceptionUtil.throwException(ErrorEnum.USER_EXISTS);
        }

        UserDO userDO = DOConverter.INSTANCE.toUserDO(userDTO);
        // 加密密码
        userDO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDO.setRole(RoleTypeEnum.USER.getName());
        userDao.insertUser(userDO);
    }

    public UserDTO getByUserName(String userName) {
        ParamChecker.checkNotBlank(userName, "userName");

        UserDO userDO = userDao.getByUserName(userName);
        return DTOConverter.INSTANCE.fromUserDO(userDO);
    }

    public UserDTO getByUserId(Long userId) {
        ParamChecker.checkNotNull(userId, "userId");

        UserDO userDO = userDao.getByUserId(userId);
        return DTOConverter.INSTANCE.fromUserDO(userDO);
    }

    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        ParamChecker.checkNotNull(userId, "userId");
        ParamChecker.checkNotBlank(newPassword, "newPassword");

        UserDO userDO = userDao.getByUserId(userId);
        if (userDO == null) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        if(!passwordEncoder.matches(oldPassword, userDO.getPassword())) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }

        newPassword = passwordEncoder.encode(newPassword);
        return userDao.updatePassword(userId, newPassword) > 0;
    }
}
