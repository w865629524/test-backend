package com.zq.backend.jwt;

import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        UserDTOWithPassword userDTO = userRepository.getWithPasswordByUserName(username);
        if (userDTO == null) {
            ExceptionUtil.throwException(ErrorEnum.USER_NOT_EXISTS);
        }
        UserExtension extension = userDTO.getExtension();
        if(Objects.isNull(extension)) {
            extension = new UserExtension();
        }
        if(Objects.isNull(extension.getJwtVersion())) {
            extension.setJwtVersion(0);
            userRepository.updateExtension(username, extension);
        }
        return new CustomUserDetails(
                userDTO.getUsername(),
                userDTO.getPassword(),
                extension.getJwtVersion(),
                AuthorityUtils.createAuthorityList("ROLE_" + userDTO.getRole()) // 添加 ROLE_ 前缀
        );
    }
}
