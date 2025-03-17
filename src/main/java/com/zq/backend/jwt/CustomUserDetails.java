package com.zq.backend.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public class CustomUserDetails extends User {
    @Serial
    private static final long serialVersionUID = 267445761086023117L;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public CustomUserDetails(String username, String password, Integer jwtVersion, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.jwtVersion = jwtVersion;
    }

    @Getter
    @Setter
    private Integer jwtVersion;
}
