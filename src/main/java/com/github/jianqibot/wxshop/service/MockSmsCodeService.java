package com.github.jianqibot.wxshop.service;

import org.springframework.stereotype.Service;

@Service
public class MockSmsCodeService implements SmsCodeService{
    @Override
    public String sendSmSCode(String tel) {
        return "000000";
    }
}
