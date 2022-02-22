package com.github.jianqibot.wxshop.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeCheckService {
    private final Map<String, String> telToCorrectUser = new ConcurrentHashMap<>();

    public void addCode(String tel, String code) {
        telToCorrectUser.put(tel, code);
    }

    public String getCorrectCode(String tel) {
        return telToCorrectUser.get(tel);
    }
}
