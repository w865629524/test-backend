package com.zq.backend.object.dto;

import com.zq.backend.object.RoleTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private RoleTypeEnum role;
    private Date createdAt;
    private Date updatedAt;
}