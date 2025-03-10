package com.zq.backend.jwt;

import com.zq.backend.Constant;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService  userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(Constant.JWT_HEADER_KEY);
        if (Objects.nonNull(token) && token.startsWith(Constant.JWT_TOKEN_PREFIX)) {
            token = token.substring(Constant.JWT_TOKEN_PREFIX.length());
            Claims claims = JwtUtil.parseJWT(token);
            if(Objects.nonNull(claims)) {
                String userName = claims.getSubject();
                if (Objects.nonNull(userName)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    if(Objects.nonNull(userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
