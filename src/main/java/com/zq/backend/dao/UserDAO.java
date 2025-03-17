package com.zq.backend.dao;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.params.ListUserParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO {

    void insertUser(UserDO userDO);

    UserDO getByUserName(String username);

    int updateUser(UserDO userDO);

    int updateExtension(String username, String extension);

    int updatePassword(String username, String password);

    List<UserDO> listUser(ListUserParam param);

    int updateRole(String username, String role);
}
