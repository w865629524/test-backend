package com.zq.backend.object.common;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Data
public class BaseResult<T> {

    private boolean success;
    private String errorCode;
    private String errorMsg;
    private T data;

    public static <T> BaseResult<T> getSuccessResult(T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> BaseResult<T> getFailedResult(@NotNull ErrorEnum errorEnum) {
        return getFailedResult(errorEnum.getErrorCode(), errorEnum.getErrorMessage());
    }

    public static <T> BaseResult<T> getFailedResult(@NotNull BaseException baseException) {
        return getFailedResult(baseException.getErrorCode(), baseException.getErrorMessage());
    }

    public static <T> BaseResult<T> getFailedResult(String errorCode, String errorMsg) {
        BaseResult<T> result = new BaseResult<>();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        return result;
    }
}
