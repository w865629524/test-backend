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

    UserVO create(RegisterPararm param);

    UserVO doLogin(LoginParam param);

    Void doLogout(String username);

    UserVO updateUser(UpdateUserParam param, String username);

    UserVO updatePassword(UpdateUserPasswordParam param, String username);

    UserVO getUserInfo(String username);

    List<UserVO> listUser(ListUserParam param);

    Void addAdmin(AddAdminParam param);
}
