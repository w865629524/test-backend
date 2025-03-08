package com.zq.backend.service;

import com.zq.backend.dao.UserDao;
import com.zq.backend.object.data.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDO user = userDao.getByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return new User(
                user.getUserName(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()) // 添加 ROLE_ 前缀
        );
    }
}
