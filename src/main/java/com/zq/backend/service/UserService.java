package com.zq.backend.service;

import com.zq.backend.converter.DTOConverter;
import com.zq.backend.dao.UserDao;
import com.zq.backend.jwt.JwtUtil;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String create(UserDTO userDTO) {
        userRepository.create(userDTO);
        UserDTO createdUser = userRepository.getByUserName(userDTO.getUserName());
        if(Objects.isNull(createdUser)) {
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
        return doLogin(userDTO.getUserName(), userDTO.getPassword());
    }

    public String doLogin(String userName, String password) {
        ParamChecker.checkNotNull(userName, "userName");
        ParamChecker.checkNotNull(password, "password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
        Authentication authentication = authenticationManager.authenticate(token);
        if(!authentication.isAuthenticated()) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }
        String jwt = JwtUtil.createJWT(authentication.getName());
        return jwt;
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        return userRepository.updatePassword(userId, oldPassword, newPassword);
    }

    public UserDTO getByUserName(String userName) {
        UserDO userDO = userDao.getByUserName(userName);
        return DTOConverter.INSTANCE.fromUserDO(userDO);
    }

    public UserDTO getUserInfo(Long userId) {
        UserDO userDO = userDao.getByUserId(userId);
        if(Objects.isNull(userDO)) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        return DTOConverter.INSTANCE.fromUserDO(userDO);
    }
}
