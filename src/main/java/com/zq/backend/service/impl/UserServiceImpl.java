package com.zq.backend.service.impl;

import com.zq.backend.Constant;
import com.zq.backend.converter.DOConverter;
import com.zq.backend.converter.ParamConverter;
import com.zq.backend.converter.VOConverter;
import com.zq.backend.jwt.JwtUtil;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.common.BaseException;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.object.params.AddAdminParam;
import com.zq.backend.object.params.ListUserParam;
import com.zq.backend.object.params.LoginParam;
import com.zq.backend.object.params.RegisterPararm;
import com.zq.backend.object.params.UpdateUserParam;
import com.zq.backend.object.params.UpdateUserPasswordParam;
import com.zq.backend.object.results.LoginResult;
import com.zq.backend.object.vo.UserVO;
import com.zq.backend.repository.UserRepository;
import com.zq.backend.service.UserService;
import com.zq.backend.utils.LogUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResult create(RegisterPararm param) {
        // 创建用户
        UserDTOWithPassword userDTO = ParamConverter.INSTANCE.toUserDTOWithPassword(param);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        userRepository.create(userDTO);

        // 查询用户
        try {
            LoginParam loginParam = new LoginParam();
            loginParam.setUsername(param.getUsername());
            loginParam.setPassword(param.getPassword());
            return doLogin(loginParam);
        } catch (BaseException e) {
            if(ErrorEnum.USER_NOT_EXISTS.getErrorCode().equals(e.getErrorCode())) {
                LogUtil.error(log, "created user not exists", e, () -> param);
                ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
            }
        }

        return null;
    }

    @Override
    public LoginResult doLogin(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if(!authentication.isAuthenticated()) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }

        UserDTO userDTO = userRepository.getByUserName(username);
        return generateTokenAndGetLoginResult(authentication, userDTO);
    }

    private LoginResult generateTokenAndGetLoginResult(Authentication authentication, UserDTO userDTO) {
        UserExtension extension = userDTO.getExtension();
        if(Objects.isNull(extension)) {
            extension = new UserExtension();
        }
        if(Objects.isNull(extension.getJwtVersion())) {
            extension.setJwtVersion(0);
            userRepository.updateExtension(userDTO.getUsername(), extension);
        }
        Date now = new Date();
        long jwtTTL = Constant.JWT_TTL;
        String token = JwtUtil.createToken(authentication.getName(), extension.getJwtVersion(), now, jwtTTL);

        LoginResult loginResult = new LoginResult();
        loginResult.setToken(token);
        loginResult.setTokenExpire(new Date(now.getTime() + jwtTTL));
        loginResult.setUserInfo(VOConverter.INSTANCE.toUserVO(userDTO));
        return loginResult;
    }

    @Override
    public Void doLogout(String username) {
        UserDTO userDTO = userRepository.getByUserName(username);
        if(Objects.isNull(userDTO)) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        UserExtension extension = userDTO.getExtension();
        if(Objects.isNull(extension)) {
            extension = new UserExtension();
        }
        if(Objects.isNull(extension.getJwtVersion())) {
            extension.setJwtVersion(0);
        } else {
            extension.setJwtVersion(extension.getJwtVersion() + 1);
        }
        int updated = userRepository.updateExtension(username, extension);
        if(updated <= 0) {
            LogUtil.error(log, "logout failed", () -> username);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
        return null;
    }

    @Override
    public UserVO updateUser(UpdateUserParam param, String username) {
        UserDO userDO = DOConverter.INSTANCE.toUserDO(param, username);
        userRepository.updateUser(userDO);
        return getUserInfo(username);
    }

    @Override
    public LoginResult updatePassword(UpdateUserPasswordParam param, String username) {
        // 检查老密码是否匹配
        UserDTOWithPassword userDTOWithPassword = userRepository.getWithPasswordByUserName(username);
        if(!passwordEncoder.matches(param.getOldPassword(), userDTOWithPassword.getPassword())) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }
        String encodedPassword = passwordEncoder.encode(param.getNewPassword());
        userRepository.updatePassword(username, encodedPassword);
        doLogout(username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, param.getNewPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDTO userDTO = userRepository.getByUserName(username);
        return generateTokenAndGetLoginResult(authentication, userDTO);
    }

    @Override
    public UserVO getUserInfo(String username) {
        UserDTO userDTO = userRepository.getByUserName(username);
        return VOConverter.INSTANCE.toUserVO(userDTO);
    }

    @Override
    public List<UserVO> listUser(ListUserParam param) {
        List<UserDTO> userDTOList = userRepository.listUser(param);
        return VOConverter.INSTANCE.toUserVOList(userDTOList);
    }

    @Override
    public Void addAdmin(AddAdminParam param) {
        UserDTO userDTO = userRepository.getByUserName(param.getNewAdminUsername());
        if(Objects.isNull(userDTO)) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        if(RoleTypeEnum.ADMIN.getWeight() <= userDTO.getRole().getWeight()) {
            return null;
        }
        int updated = userRepository.updateRole(userDTO.getUsername(), RoleTypeEnum.ADMIN);
        if(updated <= 0) {
            LogUtil.error(log, "update failed", () -> param);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
        return null;
    }
}
