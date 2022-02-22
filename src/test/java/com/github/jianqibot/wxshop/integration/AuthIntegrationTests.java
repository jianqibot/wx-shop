package com.github.jianqibot.wxshop.integration;

import com.github.jianqibot.wxshop.WxShopApplication;
import com.github.jianqibot.wxshop.api.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.http.HttpResponse;

import static com.github.jianqibot.wxshop.service.TelVerificationServiceTest.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})

public class AuthIntegrationTests extends TestHelper{
    @Autowired
    private OrderService orderService;

    @Test
    @Order(1)
    public void returnHttpOKWhenRequestParamIsValid() throws IOException, InterruptedException {
        Assertions.assertEquals(HttpStatus.OK.value(), getRegisterResponse(VALID_PARAM).statusCode()   );
    }

    @Test
    @Order(2)
    public void returnHttpBadRequestWhenRequestParamIsInvalid() throws IOException, InterruptedException {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), getRegisterResponse(INVALID_PARAM).statusCode());
    }

    @Test
    @Order(3)
    public void returnHttpBadRequestWhenRequestParamIsNull() throws IOException, InterruptedException {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), getRegisterResponse(NULL_PARAM).statusCode());
    }

    @Test
    @Order(4)
    public void loginAndLogoutTest() throws IOException, InterruptedException {

        String sessionID = getCookie(VALID_PARAM_CODE);
        Assertions.assertNotNull(sessionID);
        // check login status again
        HttpResponse<String> alreadyLoginResponse = getLoginStatusResponse();
        Assertions.assertTrue(alreadyLoginResponse.body().contains("true"));
        Assertions.assertTrue(alreadyLoginResponse.body().contains(VALID_PARAM.getTel()));
        // logout
        getLogoutResponse();
        // check logout status
        HttpResponse<String> alreadyLogoutResponse = getLoginStatusResponse();
        Assertions.assertTrue(alreadyLogoutResponse.body().contains("false"));
    }

/*    @Test //problem unsolved
    public void returnUnauthorizedWhenNotLogin() throws IOException, InterruptedException {

        HttpResponse<String> response = sendHttpGetRequestAndGetHttpResponse("/api/test");
        Assertions.assertEquals(response.statusCode(), HttpStatus.UNAUTHORIZED);
    }*/






}
