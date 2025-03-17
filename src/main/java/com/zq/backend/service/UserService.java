package com.zq.backend.service;

import com.zq.backend.object.params.LoginParam;
import com.zq.backend.object.params.RegisterPararm;
import com.zq.backend.object.params.UpdateUserParam;
import com.zq.backend.object.params.UpdateUserPasswordParam;
import com.zq.backend.object.results.LoginResult;
import com.zq.backend.object.vo.BaseUserVO;
import com.zq.backend.object.vo.UserVO;

public interface UserService {

    LoginResult create(RegisterPararm param);

    LoginResult doLogin(LoginParam param);

    Void doLogout(String username);

    UserVO updateUser(UpdateUserParam param, String username);

    BaseUserVO updatePassword(UpdateUserPasswordParam param, String username);

    UserVO getUserInfo(String username);
}
