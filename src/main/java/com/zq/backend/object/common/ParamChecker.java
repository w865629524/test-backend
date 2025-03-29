package com.zq.backend.object.common;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public class ParamChecker {

    public static void checkNotNull(Object object, String message) {
        if(Objects.isNull(object)) {
            ExceptionUtil.throwException(ErrorEnum.PARAM_ERROR, message);
        }
    }

    public static void checkNotBlank(String string, String message) {
        if(StringUtils.isBlank(string)) {
            ExceptionUtil.throwException(ErrorEnum.PARAM_ERROR, message);
        }
    }

    public static void checkNotEmpty(Collection collection, String message) {
        if(CollectionUtils.isNotEmpty(collection)) {
            ExceptionUtil.throwException(ErrorEnum.PARAM_ERROR, message);
        }
    }

    public static void checkExpression(Boolean expression, String message) {
        if(BooleanUtils.isNotTrue(expression)) {
            ExceptionUtil.throwException(ErrorEnum.PARAM_ERROR, message);
        }
    }
}
