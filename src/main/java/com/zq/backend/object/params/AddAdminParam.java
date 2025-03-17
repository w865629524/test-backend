package com.zq.backend.object.params;

import com.zq.backend.object.common.AccountValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddAdminParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 7518610530997030429L;

    private String newAdminUsername;

    @Override
    public void checkAndRevise() {
        AccountValidator.validateUsername(newAdminUsername);
    }
}
