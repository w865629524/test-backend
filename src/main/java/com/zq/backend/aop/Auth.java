package com.zq.backend.aop;


import com.zq.backend.object.RoleTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    boolean needLogin() default true;
    boolean checkUsername() default true;
    RoleTypeEnum requireRole() default RoleTypeEnum.USER;
}
