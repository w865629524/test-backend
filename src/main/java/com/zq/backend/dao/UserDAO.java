package com.zq.backend.dao;

import com.zq.backend.object.data.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDAO {

    void insertUser(UserDO userDO);

    UserDO getByUserName(String username);

    int updateUser(UserDO userDO);

    int updateExtension(String username, String extension);

    int updatePassword(String username, String password);
}
