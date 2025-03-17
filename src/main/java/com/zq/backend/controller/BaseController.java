package com.zq.backend.controller;

import com.zq.backend.object.common.BaseException;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ParamChecker;
import com.zq.backend.object.params.BaseParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Supplier;

public abstract class BaseController {

    protected <T> BaseResult<T> doHandle(Supplier<T> supplier, BaseParam... params) {
        try {
            for (BaseParam param : params) {
                ParamChecker.checkNotNull(params, "param is null");
                param.checkAndRevise();
            }
            T result = supplier.get();
            return BaseResult.getSuccessResult(result);
        } catch (Throwable t) {
            if (t instanceof BaseException) {
                return BaseResult.getFailedResult((BaseException) t);
            } else {
                return BaseResult.getFailedResult(ErrorEnum.UNKNOWN_ERROR.getErrorCode(), t.getMessage());
            }
        }
    }

    protected String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
