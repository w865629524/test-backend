package com.zq.backend.object.dto;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

@Data
public class UserExtension {
    private Integer jwtVersion;

    public static UserExtension parse(String extension) {
        return JSON.parseObject(extension, UserExtension.class);
    }
}
