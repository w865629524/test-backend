package com.zq.backend.aop;

import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.repository.UserRepository;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
public class AuthAspect {

    Logger logger = LoggerFactory.getLogger("COMMON_LOGGER");

    @Resource
    private UserRepository userRepository;

    @Around("@annotation(Auth)")
    public Object handleAuth(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Auth authAnnotation = signature.getMethod().getAnnotation(Auth.class);

        // 无需登录的接口不进行校验
        boolean needLogin = Optional.ofNullable(authAnnotation).map(Auth::needLogin).orElse(false);
        if(!needLogin) {
            return pjp.proceed();
        }

        // 验证用户是否已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return BaseResult.getFailedResult(ErrorEnum.NEED_LOGIN);
        }

        // 校验权限是否匹配
        UserDTO authUser = userRepository.getByUserName(authentication.getName());
        RoleTypeEnum requireRole = authAnnotation.requireRole();
        if(Objects.nonNull(requireRole) && requireRole.getWeight() > authUser.getRole().getWeight()) {
            return BaseResult.getFailedResult(ErrorEnum.NO_PERMISSION);
        }

        return pjp.proceed();
    }
}
