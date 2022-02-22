package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.entity.TelAndCode;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerificationService {
    private static final Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");
    /**
     * tel must exist and be valid
     * @param param telephone number and verification code
     * @return true if param are valid
     */
    public boolean verifyTelParam(TelAndCode param){
        if (param == null || param.getTel() == null){
            return false;
        } else {
            return TEL_PATTERN.matcher(param.getTel()).find();
        }
    }
}
