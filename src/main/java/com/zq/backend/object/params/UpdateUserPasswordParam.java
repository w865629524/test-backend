package com.zq.backend.object.params;

import com.alibaba.druid.util.StringUtils;
import com.zq.backend.object.common.AccountValidator;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateUserPasswordParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 7969941556440413411L;

    private String oldPassword;
    private String newPassword;

    @Override
    public void checkAndRevise() {
        if(StringUtils.equals(oldPassword, newPassword)) {
            ExceptionUtil.throwException(ErrorEnum.PARAM_ERROR);
        }
        AccountValidator.validatePassword(newPassword);
    }
}
