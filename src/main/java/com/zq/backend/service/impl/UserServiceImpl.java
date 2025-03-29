package com.zq.backend.service.impl;

import com.zq.backend.Constant;
import com.zq.backend.context.ControllerContext;
import com.zq.backend.context.UserContext;
import com.zq.backend.converter.DOConverter;
import com.zq.backend.converter.ParamConverter;
import com.zq.backend.converter.VOConverter;
import com.zq.backend.jwt.JwtUtil;
import com.zq.backend.object.enums.RoleTypeEnum;
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
import com.zq.backend.object.vo.UserVO;
import com.zq.backend.repository.UserRepository;
import com.zq.backend.service.UserService;
import com.zq.backend.utils.LogUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    public void create(RegisterPararm param) {
        // 创建用户
        UserDTOWithPassword userDTO = ParamConverter.INSTANCE.toUserDTOWithPassword(param);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        int updated = userRepository.create(userDTO);
        if(updated <= 0) {
            LogUtil.error(log, "create failed", () -> param);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    public UserVO doLogin(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        UserDTO userDTO = userRepository.getByUserName(username);
        if(Objects.isNull(userDTO)) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if(!authentication.isAuthenticated()) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return VOConverter.INSTANCE.toUserVO(userDTO);
    }

    @Override
    public void createToken(String username) {
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
            userRepository.updateExtension(userDTO.getUsername(), extension);
        }
        Date now = new Date();
        long jwtTTL = Constant.JWT_TTL_MILLIS;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = JwtUtil.createToken(authentication.getName(), extension.getJwtVersion(), now, jwtTTL);

        ResponseCookie responseCookie = ResponseCookie.from(Constant.JWT_TOKEN_KEY, token)
                // 禁止JavaScript访问Cookie，防止XSS攻击
                .httpOnly(true)
                // 只允许通过HTTPS传输，防止中间人攻击
//                .secure(true)
                // 指定Cookie的路径范围
                .path("/")
                // 控制Cookie是否可以在跨站请求中发送，防止CSRF攻击
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                // 指定Cookie的有效期（秒）
                .maxAge(Duration.ofMillis(jwtTTL))
                // 指定Cookie的作用域（域名范围）
//                .domain()
                .build();
        ControllerContext.data().addCookie(responseCookie);
    }

    @Override
    public void cancelToken() {
        ResponseCookie responseCookie = ResponseCookie.from(Constant.JWT_TOKEN_KEY, "")
                .maxAge(0)
                .build();
        ControllerContext.data().addCookie(responseCookie);
    }

    @Override
    public void doLogout(String username) {
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
        int updated = userRepository.updateExtension(userDTO.getUsername(), extension);
        if(updated <= 0) {
            LogUtil.error(log, "logout failed", userDTO::getUsername);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    public UserVO updateUser(UpdateUserParam param) {
        String username = UserContext.data().getUsername();
        UserDO userDO = DOConverter.INSTANCE.toUserDO(param, username);
        userRepository.updateUser(userDO);
        return getUserInfo(username);
    }

    @Override
    public void updatePassword(UpdateUserPasswordParam param) {
        // 检查老密码是否匹配
        String username = UserContext.data().getUsername();
        UserDTOWithPassword userDTOWithPassword = userRepository.getWithPasswordByUserName(username);
        if(!passwordEncoder.matches(param.getOldPassword(), userDTOWithPassword.getPassword())) {
            ExceptionUtil.throwException(ErrorEnum.WRONG_PASSWORD);
        }
        String encodedPassword = passwordEncoder.encode(param.getNewPassword());
        int updated = userRepository.updatePassword(username, encodedPassword);
        if(updated <= 0) {
            LogUtil.error(log, "update failed", () -> param);
            ExceptionUtil.throwException(ErrorEnum.DB_ERROR);
        }
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
    public void addAdmin(AddAdminParam param) {
        UserDTO userDTO = userRepository.getByUserName(param.getNewAdminUsername());
        if(Objects.isNull(userDTO)) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        if(RoleTypeEnum.ADMIN.getWeight() <= userDTO.getRole().getWeight()) {
            return;
        }
        int updated = userRepository.updateRole(userDTO.getUsername(), RoleTypeEnum.ADMIN);
        if(updated <= 0) {
            LogUtil.error(log, "update failed", () -> param);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
        }
    }
}
