package com.zq.backend.object.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTOWithPassword extends UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7666081500979927288L;

    private String password;
}
