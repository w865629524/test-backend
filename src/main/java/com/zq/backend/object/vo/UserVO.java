package com.zq.backend.object.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends BaseUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4906375610910231517L;

    private String email;
    private String phone;
    private String address;
}