package com.zq.backend.object.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 528888419836255125L;

    private String username;
    private String password;

    @Override
    public void checkAndRevise() {
    }
}
