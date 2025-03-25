package com.zq.backend.utils;

import com.alibaba.druid.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class HTTPUtil {

    public static String getValueFromCookies(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if(Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if(StringUtils.equals(key, cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
