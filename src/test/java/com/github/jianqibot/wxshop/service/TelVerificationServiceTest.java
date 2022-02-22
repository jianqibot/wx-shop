package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.entity.TelAndCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TelVerificationServiceTest {
    public static final TelAndCode VALID_PARAM = new TelAndCode("13812345678", "null");
    public static final TelAndCode NULL_PARAM = new TelAndCode("null", "null");
    public static final TelAndCode INVALID_PARAM = new TelAndCode("123456", "null");
    public static final TelAndCode VALID_PARAM_CODE = new TelAndCode("13812345678", "000000");


    @Test
    public void returnTrueIfParamIsValid() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParam(VALID_PARAM));
    }

    @Test
    public void returnFalseIfTelIsNull() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParam(NULL_PARAM));
    }

    @Test
    public void returnFalseIfTelIsNotValid() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParam(INVALID_PARAM));
    }
}
