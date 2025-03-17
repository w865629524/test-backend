package com.zq.backend.object.dto;

import com.zq.backend.object.RoleTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String avator;
    private String nick;
    private String email;
    private String phone;
    private String address;
    private RoleTypeEnum role;
    private UserExtension extension;
    private Date createdAt;
    private Date updatedAt;
}