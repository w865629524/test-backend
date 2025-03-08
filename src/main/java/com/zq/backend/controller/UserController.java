package com.zq.backend.controller;

import com.zq.backend.converter.DTOConverter;
import com.zq.backend.converter.VOConverter;
import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.common.BaseException;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.zq.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private <RESULTVO, RESULTDTO> ResponseEntity doHandle(Supplier<RESULTDTO> supplier, Function<RESULTDTO, RESULTVO> resultConverter,
                                                          boolean requireLogin, Long requireUserId, RoleTypeEnum requireRole) {
        try {
            if(Objects.nonNull(requireUserId) || Objects.nonNull(requireRole)) {
                // 验证用户是否已登录
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    return ResponseEntity.status(401).body("用户未登录！");
                }
                UserDTO authUser = userService.getByUserName(authentication.getName());
                if(requireLogin && Objects.isNull(authUser)) {
                    ExceptionUtil.throwException(ErrorEnum.NEED_LOGIN);
                }

                // 验证用户 ID 是否匹配
                if(requireLogin && Objects.nonNull(requireUserId) && !Objects.equals(authUser.getId(), requireUserId)) {
                    ExceptionUtil.throwException(ErrorEnum.LOGIN_ERROR);
                }

                if(requireLogin && Objects.nonNull(requireRole) && !RoleTypeEnum.STRANGER.equals(requireRole) && authUser.getRole().getWeight() < requireRole.getWeight()) {
                    ExceptionUtil.throwException(ErrorEnum.NO_PERMISSION);
                }
            }
            RESULTDTO result = supplier.get();
            if(Objects.isNull(result) || Objects.isNull(resultConverter)) {
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.ok(resultConverter.apply(result));
        } catch (BaseException e) {
            BaseResult<Object> failedResult = BaseResult.getFailedResult(e);
            return ResponseEntity.badRequest().body(failedResult);
        } catch (Throwable t) {
            return ResponseEntity.badRequest().body(t.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserVO userVO) {
        return doHandle(() -> userService.create(DTOConverter.INSTANCE.fromUserVO(userVO)), null, false, null, null);
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String userName, @RequestParam String password) {
        return doHandle(() -> userService.doLogin(userName, password), null, false, null, null);
    }

    @PutMapping("/change-password")
    public ResponseEntity changePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return doHandle(() -> userService.changePassword(userId, oldPassword, newPassword), null, true, userId, RoleTypeEnum.USER);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserInfo(@PathVariable Long userId) {
        return doHandle(() -> userService.getUserInfo(userId), VOConverter.INSTANCE::toUserVO, true, userId, RoleTypeEnum.USER);
    }
}
