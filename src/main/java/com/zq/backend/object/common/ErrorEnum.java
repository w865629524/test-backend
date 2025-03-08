package com.zq.backend.object.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    PARAM_ERROR("10000", "参数错误"),
    NEED_LOGIN("10001", "用户未登录"),
    LOGIN_ERROR("10002", "登录状态错误"),
    NO_PERMISSION("10003", "权限不足"),
    USER_EXISTS("10004", "用户已存在"),
    USER_NOT_EXISTS("10005", "用户不存在"),
    WRONG_PASSWORD("10006", "密码错误"),
    UNKNOWN_ERROR("99999", "未知错误"),
    ;

    private final String errorCode;
    private final String errorMessage;
}
