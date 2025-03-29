package com.zq.backend.context;

import lombok.Data;

import java.util.Objects;

public class UserContext {

    private static final ThreadLocal<UserContextData> context = new ThreadLocal<>();

    public static UserContextData data() {
        UserContextData data = context.get();
        if(Objects.isNull(data)) {
            data = new UserContextData();
            context.set(data);
        }
        return data;
    }

    public static void reset() {
        context.remove();
    }

    @Data
    public static class UserContextData {

        private String username;
    }
}
