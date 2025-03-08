package com.zq.backend.object.common;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExceptionUtil {

    public static void throwException(@NotNull ErrorEnum errorEnum) {
        throwException(errorEnum, null);
    }

    public static void throwException(@NotNull ErrorEnum errorEnum, String errorMessageExt) {
        throw new BaseException(errorEnum.getErrorCode(), getErrorMessage(errorEnum, errorMessageExt));
    }

    private static String getErrorMessage(@NotNull ErrorEnum errorEnum, String errorMessageExt) {
        if(Objects.isNull(errorMessageExt)) {
            return errorEnum.getErrorMessage();
        }
        return String.format("[msg:%s][ext:%s]", errorEnum.getErrorMessage(), errorEnum.getErrorCode());
    }
}
