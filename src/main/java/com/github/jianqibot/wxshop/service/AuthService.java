package com.github.jianqibot.wxshop.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class AuthService {
    private final UserService userService;
    private final VerificationCodeCheckService codeCheckService;
    private final SmsCodeService smsCodeService;

    @Inject
    public AuthService(UserService userService,
                       VerificationCodeCheckService codeCheckService,
                       SmsCodeService smsCodeService) {
        this.userService = userService;
        this.codeCheckService = codeCheckService;
        this.smsCodeService = smsCodeService;
    }

    public void sendVerificationCode(String tel) {
        userService.createUserIfNotExist(tel);
        String code = smsCodeService.sendSmSCode(tel);
        codeCheckService.addCode(tel, code);
    }
}
