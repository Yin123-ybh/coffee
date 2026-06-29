package com.ybh.coffee.common.context;

public final class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private UserContext() {
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        Long userId = USER_ID.get();
        return userId == null ? 1L : userId;
    }

    public static void clear() {
        USER_ID.remove();
    }
}
