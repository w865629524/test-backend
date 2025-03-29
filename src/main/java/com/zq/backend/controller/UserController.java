package com.zq.backend.controller;

import com.zq.backend.aop.Auth;
import com.zq.backend.context.UserContext;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.params.AddAdminParam;
import com.zq.backend.object.params.ListUserParam;
import com.zq.backend.object.params.LoginParam;
import com.zq.backend.object.params.RegisterPararm;
import com.zq.backend.object.params.UpdateUserParam;
import com.zq.backend.object.params.UpdateUserPasswordParam;
import com.zq.backend.object.vo.UserVO;
import com.zq.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseResult<UserVO>> register(@RequestBody RegisterPararm param) {
        return doHandle(() -> {
            userService.create(param);
            userService.createToken(param.getUsername());
            return userService.getUserInfo(param.getUsername());
        }, "/api/user/register", param);
    }

    @Auth(needLogin = false, requireRole = RoleTypeEnum.STRANGER)
    @PostMapping("/login")
    public ResponseEntity<BaseResult<UserVO>> login(@RequestBody LoginParam param) {
        return doHandle(() -> {
            UserVO userVO = userService.doLogin(param);
            userService.createToken(param.getUsername());
            return userVO;
        }, "/api/user/login", param);
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @RequestMapping(value="/logout", method={RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<BaseResult<Void>> logout() {
        return doHandle(() -> {
            userService.doLogout(UserContext.data().getUsername());
            userService.cancelToken();
            return null;
        }, "/api/user/logout");
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @PostMapping("/update")
    public ResponseEntity<BaseResult<UserVO>> update(@RequestBody UpdateUserParam param) {
        return doHandle(() -> userService.updateUser(param), "/api/user/update", param);
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @PostMapping("/update/password")
    public ResponseEntity<BaseResult<UserVO>> updatePassword(@RequestBody UpdateUserPasswordParam param) {
        return doHandle(() -> {
            userService.updatePassword(param);

            String username = UserContext.data().getUsername();
            userService.doLogout(username);
            userService.createToken(username);
            return userService.getUserInfo(username);
        }, "/api/user/updatePassword", param);
    }

    @Auth(requireRole = RoleTypeEnum.USER)
    @RequestMapping(value="/info", method={RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<BaseResult<UserVO>> info() {
        return doHandle(() -> userService.getUserInfo(UserContext.data().getUsername()), "/api/user/info");
    }

    @Auth(requireRole = RoleTypeEnum.ADMIN)
    @PostMapping("/list")
    public ResponseEntity<BaseResult<List<UserVO>>> list(@RequestBody ListUserParam param) {
        return doHandle(() -> userService.listUser(param), "/api/user/list", param);
    }

    @Auth(requireRole = RoleTypeEnum.ADMIN)
    @PostMapping("/addAdmin")
    public ResponseEntity<BaseResult<Void>> addAdmin(@RequestBody AddAdminParam param) {
        return doHandle(() -> {
            userService.addAdmin(param);
            return null;
        }, "/api/user/addAdmin", param);
    }
}
