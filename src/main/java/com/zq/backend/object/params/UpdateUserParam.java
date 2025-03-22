package com.zq.backend.object.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateUserParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1900766834924266450L;

    private String newNick;
    private String newAvatar;
    private String newEmail;

    @Override
    public void checkAndRevise() {
    }
}
