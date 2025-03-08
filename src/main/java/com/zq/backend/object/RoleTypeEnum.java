package com.zq.backend.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    STRANGER("stranger", 0),

    USER("user", 1),

    ADMIN("admin", 10),
    ;

    private final String name;
    private final Integer weight;

    public static RoleTypeEnum parse(String role) {
        for (RoleTypeEnum type : RoleTypeEnum.values()) {
            if (type.getName().equals(role)) {
                return type;
            }
        }
        return null;
    }
}
