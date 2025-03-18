package com.zq.backend.converter;

import com.alibaba.fastjson2.JSON;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.enums.UserStatusEnum;
import org.mapstruct.Named;

import java.util.Objects;

public class ConverterWorker {
    @Named("toNotEmptyJsonString")
    public String toNotEmptyJsonString(Object obj) {
        if (Objects.isNull(obj)) {
            return "{}";
        }
        return JSON.toJSONString(obj);
    }

    @Named("parseUserStatusEnum")
    public UserStatusEnum parseUserStatusEnum(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return UserStatusEnum.parse(value);
    }

    @Named("parseRoleTypeEnum")
    public RoleTypeEnum parseRoleTypeEnum(String str) {
        if (Objects.isNull(str)) {
            return null;
        }
        return RoleTypeEnum.parse(str);
    }

    @Named("checkIsAdmin")
    public boolean checkIsAdmin(RoleTypeEnum role) {
        return Objects.nonNull(role) && RoleTypeEnum.ADMIN.equals(role);
    }
}
