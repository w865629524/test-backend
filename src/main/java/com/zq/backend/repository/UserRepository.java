package com.zq.backend.repository;

import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.object.params.ListUserParam;

import java.util.List;

public interface UserRepository {
    void create(UserDTOWithPassword userDTO);

    UserDTO getByUserName(String username);

    UserDTOWithPassword getWithPasswordByUserName(String username);

    int updateExtension(String username, UserExtension userExtension);

    int updateUser(UserDO userDO);

    int updatePassword(String username, String encodedPassword);

    List<UserDTO> listUser(ListUserParam param);

    int updateRole(String username, RoleTypeEnum roleTypeEnum);
}
