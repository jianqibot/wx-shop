package com.github.jianqibot.wxshop.service;

public interface SmsCodeService {
    // send sms code to user's phone tel
    String sendSmSCode(String tel);
}
