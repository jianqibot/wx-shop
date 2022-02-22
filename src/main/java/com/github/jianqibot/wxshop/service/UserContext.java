package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.generate.User;

public class UserContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }
}
