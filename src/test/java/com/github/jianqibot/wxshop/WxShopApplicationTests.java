package com.github.jianqibot.wxshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class WxShopApplicationTests {
    @Test
    void contextLoads() {

    }
}
