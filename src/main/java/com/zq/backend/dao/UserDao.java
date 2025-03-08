package com.zq.backend.dao;

import com.zq.backend.object.data.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    // 插入新用户
    void insertUser(UserDO user);

    // 根据用户名查询用户
    UserDO getByUserName(String userName);

    // 根据用户 ID 查询用户
    UserDO getByUserId(Long id);

    int updatePassword(Long id, String newPassword);
}
