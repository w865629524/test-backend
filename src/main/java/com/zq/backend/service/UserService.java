package com.zq.backend.service;

import com.zq.backend.object.params.AddAdminParam;
import com.zq.backend.object.params.ListUserParam;
import com.zq.backend.object.params.LoginParam;
import com.zq.backend.object.params.RegisterPararm;
import com.zq.backend.object.params.UpdateUserParam;
import com.zq.backend.object.params.UpdateUserPasswordParam;
import com.zq.backend.object.vo.UserVO;

import java.util.List;

public interface UserService {

    void create(RegisterPararm param);

    UserVO doLogin(LoginParam param);

    void createToken(String username);

    void cancelToken();

    void doLogout(String username);

    UserVO updateUser(UpdateUserParam param);

    void updatePassword(UpdateUserPasswordParam param);

    UserVO getUserInfo(String username);

    List<UserVO> listUser(ListUserParam param);

    void addAdmin(AddAdminParam param);
}
