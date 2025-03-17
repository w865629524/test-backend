package com.zq.backend.object.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListUserParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1564713851311881871L;

    private String nick;
    private String email;
    private String phone;
    private String address;
    private Boolean isAdmin;

    @Override
    public void checkAndRevise() {
    }
}
