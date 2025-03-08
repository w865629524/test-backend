package com.zq.backend.object.data;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String role;
    private Date createdAt;
    private Date updatedAt;
}