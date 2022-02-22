package com.github.jianqibot.wxshop.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    private UserService userService;

    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object tel = SecurityUtils.getSubject().getPrincipal();
        // user login already
        if (tel != null){
            userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // clean context, very important!
        UserContext.setCurrentUser(null);
    }
}
