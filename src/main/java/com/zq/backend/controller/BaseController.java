package com.zq.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.zq.backend.object.common.BaseException;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.params.BaseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public abstract class BaseController {

    protected <T> BaseResult<T> doHandle(Action<T> action, String methodName, BaseParam... params) {
        String username = getUsername();
        try {
            log.info("[{}][{}][msg: invock][username: {}]", getClassName(), methodName, username);
            for (BaseParam param : params) {
                ParamChecker.checkNotNull(params, "param is null");
                param.checkAndRevise();
            }
            T result = action.execute();
            log.info("[{}][{}][msg: return][result:{}][username: {}]", getClassName(), methodName, JSON.toJSONString(result), username);
            return BaseResult.getSuccessResult(result);
        } catch (Throwable t) {
            BaseResult<T> result;
            String errorMsg;
            if (t instanceof BaseException) {
                result = BaseResult.getFailedResult((BaseException) t);
                errorMsg = ((BaseException) t).getErrorMessage();
            } else {
                result = BaseResult.getFailedResult(ErrorEnum.UNKNOWN_ERROR.getErrorCode(), t.getMessage());
                errorMsg = t.getMessage();
            }
            log.info("[{}][{}][msg: error][errorMsg: {}][result: {}][username: {}]", getClassName(), methodName, errorMsg, JSON.toJSONString(result), username);
            return result;
        }
    }

    protected String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
