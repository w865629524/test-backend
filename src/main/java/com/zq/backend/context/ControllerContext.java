package com.zq.backend.context;

import lombok.Data;
import org.springframework.http.ResponseCookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ControllerContext {

    private static final ThreadLocal<ControllerContextData> context = new ThreadLocal<>();

    public static ControllerContextData data() {
        ControllerContextData data = context.get();
        if(Objects.isNull(data)) {
            data = new ControllerContextData();
            context.set(data);
        }
        return data;
    }

    public static void reset() {
        context.remove();
    }

    @Data
    public static class ControllerContextData {

        private List<ResponseCookie> responseCookieList = new ArrayList<>();

        public void addCookie(ResponseCookie responseCookie) {
            responseCookieList.add(responseCookie);
        }
    }
}
