package com.zq.backend.object.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    NORMAL(0),

    DELETED(1),
    ;

    private final Integer value;

    public static UserStatusEnum parse(Integer value) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.getValue().equals(value)) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
