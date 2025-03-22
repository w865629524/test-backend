package com.zq.backend.object.data;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String avatar;
    private String nick;
    private String email;
    private String role;
    private String extension;
    private Date createdAt;
    private Date updatedAt;
}