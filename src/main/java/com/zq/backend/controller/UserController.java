package com.zq.backend.controller;

import com.zq.backend.aop.Auth;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.params.AddAdminParam;
import com.zq.backend.object.params.ListUserParam;
import com.zq.backend.object.params.LoginParam;
import com.zq.backend.object.params.RegisterPararm;
import com.zq.backend.object.params.UpdateUserParam;
import com.zq.backend.object.params.UpdateUserPasswordParam;
import com.zq.backend.object.results.LoginResult;
import com.zq.backend.object.vo.UserVO;
import com.zq.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Auth(needLogin = false, requireRole = RoleTypeEnum.STRANGER)
    @PostMapping("/register")
    public BaseResult<LoginResult> register(@RequestBody RegisterPararm param) {
        return doHandle(() -> userService.create(param), param);
    }

    @Auth(needLogin = false, requireRole = RoleTypeEnum.STRANGER)
    @PostMapping("/login")
    public BaseResult<LoginResult> login(@RequestBody LoginParam param) {
        return doHandle(() -> userService.doLogin(param), param);
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @RequestMapping(value="/logout", method={RequestMethod.GET, RequestMethod.POST})
    public BaseResult<Void> logout() {
        return doHandle(() -> userService.doLogout(getUsername()));
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @PostMapping("/update")
    public BaseResult<UserVO> update(@RequestBody UpdateUserParam param) {
        return doHandle(() -> userService.updateUser(param, getUsername()));
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @PostMapping("/update/password")
    public BaseResult<LoginResult> updatePassword(@RequestBody UpdateUserPasswordParam param) {
        return doHandle(() -> userService.updatePassword(param, getUsername()));
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @RequestMapping(value="/info", method={RequestMethod.GET, RequestMethod.POST})
    public BaseResult<UserVO> info() {
        return doHandle(() -> userService.getUserInfo(getUsername()));
    }

    @Auth(requireRole = RoleTypeEnum.ADMIN)
    @PostMapping("/list")
    public BaseResult<List<UserVO>> list(@RequestBody ListUserParam param) {
        return doHandle(() -> userService.listUser(param));
    }

    @Auth(requireRole = RoleTypeEnum.ADMIN)
    @PostMapping("/addAdmin")
    public BaseResult<Void> addAdmin(@RequestBody AddAdminParam param) {
        return doHandle(() -> userService.addAdmin(param));
    }
}
