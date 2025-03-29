package com.zq.backend;

import java.util.Base64;

public class Constant {
    public static final long ONE_SECOND_MILLS = 1000L;
    public static final long ONE_MINUTE_MILLS = ONE_SECOND_MILLS * 60;
    public static final long ONE_HOUR_MILLS = ONE_MINUTE_MILLS * 60;
    public static final long ONE_DAY_MILLS = ONE_HOUR_MILLS * 24;

    public static final String JWT_TOKEN_KEY = "jtk";
    public static final String JWT_VERSION_KEY = "jwtVersion";
    public static final String JWT_TOKEN_PREFIX = "ZQ";
    public static final long JWT_TTL_MILLIS = ONE_DAY_MILLS * 14;  // 有效期14天
    private static final String JWT_RAW_KEY = "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac";
    public static final String JWT_KEY = Base64.getEncoder().encodeToString(JWT_RAW_KEY.getBytes());
    public static final String JWT_ALGORITHM = "HmacSHA256";
}
