package com.zq.backend.object.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2181054908601299020L;

    private String username;
    private String avator;
    private String nick;
    private Boolean isAdmin;
}
