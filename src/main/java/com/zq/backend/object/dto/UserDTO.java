package com.zq.backend.object.dto;

import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.enums.UserStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private UserStatusEnum status;
    private String avatar;
    private String nick;
    private String email;
    private RoleTypeEnum role;
    private UserExtension extension;
    private Date createdAt;
    private Date updatedAt;
}