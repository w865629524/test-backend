package com.zq.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.zq.backend.context.ControllerContext;
import com.zq.backend.context.UserContext;
import com.zq.backend.object.common.BaseException;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.params.BaseParam;
import com.zq.backend.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
public abstract class BaseController {

    protected <T> ResponseEntity<BaseResult<T>> doHandle(Action<T> action, String methodName, BaseParam... params) {
        beforeHandle(methodName, params);

        BaseResult<T> result = null;
        String errorMsg = null;
        try {
            result = BaseResult.getSuccessResult(action.execute());
        } catch (Throwable t) {
            if (t instanceof BaseException) {
                result = BaseResult.getFailedResult((BaseException) t);
                errorMsg = ((BaseException) t).getErrorMessage();
            } else {
                result = BaseResult.getFailedResult(ErrorEnum.UNKNOWN_ERROR.getErrorCode(), t.getMessage());
                errorMsg = t.getMessage();
            }
        } finally {
            afterHandle(methodName, result, errorMsg, params);
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        List<ResponseCookie> responseCookies = Optional.ofNullable(ControllerContext.data())
                .map(ControllerContext.ControllerContextData::getResponseCookieList)
                .orElse(new ArrayList<>());
        Set<String> addedCookies = new HashSet<>();
        for (ResponseCookie responseCookie : responseCookies) {
            String cookieName = responseCookie.getName();
            if(addedCookies.contains(cookieName)) {
                LogUtil.warn(log, "same cookie have added", () -> cookieName);
                continue;
            }
            addedCookies.add(cookieName);
            responseBuilder.header(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
        return responseBuilder.body(result);
    }

    private void beforeHandle(String methodName, BaseParam... params) {
        UserContext.reset();
        ControllerContext.reset();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserContext.data().setUsername(username);

        log.info("[{}][{}][msg: invock][username: {}]", getClassName(), methodName, username);
        for (BaseParam param : params) {
            ParamChecker.checkNotNull(params, "param is null");
            param.checkAndRevise();
        }
    }

    private <T> void afterHandle(String methodName, BaseResult<T> result, String errorMsg, BaseParam... params) {
        String username = UserContext.data().getUsername();
        if(Objects.nonNull(result) && result.isSuccess()) {
            log.info("[{}][{}][msg: return][executeResult:{}][username: {}]", getClassName(), methodName, JSON.toJSONString(result), username);
        } else {
            log.error("[{}][{}][msg: error][errorMsg: {}][result: {}][username: {}]", getClassName(), methodName, errorMsg, JSON.toJSONString(result), username);
        }
    }

    private static String className = null;

    protected String getClassName() {
        if(Objects.nonNull(className)) {
            return className;
        }
        return className = this.getClass().getSimpleName();
    }
}
