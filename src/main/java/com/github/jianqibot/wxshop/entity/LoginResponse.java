package com.github.jianqibot.wxshop.entity;

import com.github.jianqibot.wxshop.generate.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("EI_EXPOSE_REP")
public class LoginResponse {
    private boolean login;
    private User user;

    public LoginResponse() {
    }

    private LoginResponse(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public static LoginResponse notLoginResponse() {
        return new LoginResponse(false, null);
    }

    public static LoginResponse alreadyLoginResponse(User user) {
        return new LoginResponse(true, user);
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return user;
    }
}
