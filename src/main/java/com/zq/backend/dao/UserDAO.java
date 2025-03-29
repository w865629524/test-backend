package com.zq.backend.dao;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.params.ListUserParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO {

    int insertUser(UserDO userDO);

    UserDO getByUserName(@Param("username") String username);

    UserDO getByUserNameIgnoreStatus(@Param("username") String username);

    int updateUser(UserDO userDO);

    int updateExtension(@Param("username") String username, @Param("extension") String extension);

    int updatePassword(@Param("username") String username, @Param("password") String password);

    List<UserDO> listUser(ListUserParam param);

    int updateRole(@Param("username") String username, @Param("role") String role);
}
