package com.zq.backend.object.params;

import com.zq.backend.object.common.AccountValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterPararm extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 5126397333748576951L;

    private String username;
    private String password;
    private String nick;
    private String avator;
    private String email;
    private String phone;
    private String address;

    @Override
    public void checkAndRevise() {
        AccountValidator.validateUsername(username);
        AccountValidator.validatePassword(password);
        if(StringUtils.isBlank(nick)) {
            nick = username;
        }
    }
}
