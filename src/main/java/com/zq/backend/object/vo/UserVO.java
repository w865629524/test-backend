package com.zq.backend.object.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4906375610910231517L;

    private String username;
    private String avatar;
    private String nick;
    private Boolean isAdmin;
    private String email;
}