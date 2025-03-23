package com.zq.backend.utils;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class LogUtil {

    public static void info(Logger logger, String msg, Supplier<?>... params) {
        try {
            StackTraceElement caller = getCallerInfo();

            // 格式化日志内容
            String formattedMessage = formatLogMessage(caller.getClassName(), caller.getMethodName(),
                    caller.getLineNumber(), msg, null, params);

            // 使用 SLF4J 的 Logger 输出日志
            logger.info(formattedMessage);
        } catch (Exception e) {
            logger.error("[LogUtil][info][msg: unknown error] e:", e);
        }
    }

    public static void warn(Logger logger, String msg, Supplier<?>... params) {
        warn(logger, msg, null, params);
    }

    public static void warn(Logger logger, String msg, Throwable e, Supplier<?>... params) {
        try {
            StackTraceElement caller = getCallerInfo();

            // 格式化日志内容
            String formattedMessage = formatLogMessage(caller.getClassName(), caller.getMethodName(),
                    caller.getLineNumber(), msg, e, params);

            // 使用 SLF4J 的 Logger 输出日志
            if (e == null) {
                logger.warn(formattedMessage);
            } else {
                logger.warn(formattedMessage, e);
            }
        } catch (Exception exception) {
            logger.error("[LogUtil][warn][msg: unknown error] e:", exception);
        }
    }

    public static void error(Logger logger, String msg, Supplier<?>... params) {
        error(logger, msg, null, params);
    }

    public static void error(Logger logger, String msg, Throwable e, Supplier<?>... params) {
        try {
            StackTraceElement caller = getCallerInfo();

            // 格式化日志内容
            String formattedMessage = formatLogMessage(caller.getClassName(), caller.getMethodName(),
                    caller.getLineNumber(), msg, e, params);

            // 使用 SLF4J 的 Logger 输出日志
            if (e == null) {
                logger.error(formattedMessage);
            } else {
                logger.error(formattedMessage, e);
            }
        } catch (Exception exception) {
            logger.error("[LogUtil][error][msg: unknown error] e:", exception);
        }
    }

    private static String formatLogMessage(String className, String methodName, int lineNumber,
                                           String msg, Throwable e, Supplier<?>... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(className).append("]")
                .append("[").append(methodName).append(":").append(lineNumber).append("]")
                .append("[msg: ").append(msg).append("]");

        // 拼接参数
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                String fieldName = "param" + (i+1);
                Supplier<?> param = params[i];
                try {
                    String expression = param.toString();
                    String[] parts = expression.split("\\$");
                    if(parts.length > 0) {
                        fieldName = parts[parts.length - 1].replaceAll("\\d+", "").trim();
                    }
                } catch (Exception ignore) {}
                Object paramValue = param.get();
                String valueStr = JSON.toJSONString(paramValue);
                sb.append("[").append(fieldName).append(": ").append(valueStr).append("]");
            }
        }

        // 拼接异常信息
        if (e != null) {
            sb.append(" e: ");
        }

        return sb.toString();
    }

    private static StackTraceElement getCallerInfo() {
        // 获取当前线程的堆栈信息
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // 遍历堆栈，找到调用 LogUtil 的地方
        for (StackTraceElement element : stackTrace) {
            // 跳过 LogUtil 类本身的调用
            if (!element.getClassName().equals(LogUtil.class.getName())) {
                return element;
            }
        }

        // 如果没有找到，返回一个默认值
        return new StackTraceElement("UnknownClass", "unknownMethod", "UnknownFile", -1);
    }
}
