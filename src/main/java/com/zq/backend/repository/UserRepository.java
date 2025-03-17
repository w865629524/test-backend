package com.zq.backend.repository;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;

public interface UserRepository {
    void create(UserDTOWithPassword userDTO);

    UserDTO getByUserName(String username);

    UserDTOWithPassword getWithPasswordByUserName(String username);

    int updateExtension(String username, UserExtension userExtension);

    int updateUser(UserDO userDO);

    int updatePassword(String username, String encodedPassword);
}
