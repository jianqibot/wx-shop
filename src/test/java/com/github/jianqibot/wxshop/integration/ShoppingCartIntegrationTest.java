package com.github.jianqibot.wxshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jianqibot.wxshop.WxShopApplication;
import com.github.jianqibot.wxshop.entity.GoodsInShoppingCart;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.entity.Response;
import com.github.jianqibot.wxshop.entity.ShoppingCartResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.http.HttpResponse;

import static com.github.jianqibot.wxshop.service.TelVerificationServiceTest.VALID_PARAM;
import static com.github.jianqibot.wxshop.service.TelVerificationServiceTest.VALID_PARAM_CODE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
public class ShoppingCartIntegrationTest extends TestHelper {

    @Test
    public void shoppingCartTest() throws IOException, InterruptedException {
        // register
        getRegisterResponse(VALID_PARAM);
        // login
        getLoginResponse(VALID_PARAM_CODE);
        // get shopping cart
        HttpResponse<String> getShoppingCartByUserIdResponse = getGetShoppingCartByUserIdResponse(1, 10);
        PageResponse<ShoppingCartResponse> result = parseJsonObject(getShoppingCartByUserIdResponse.body(), new TypeReference<>() {
        });
        assertEquals(getShoppingCartByUserIdResponse.statusCode(), HttpStatus.OK.value());
        assertEquals(result.getTotalPage(), 1);
        assertEquals(result.getData().size(), 2);
        assertEquals(result.getData().stream().filter(data -> data.getShop().getId() == 11L).findAny().get().getGoods().size(), 3);
        assertEquals(result.getData().stream().filter(data -> data.getShop().getId() == 22L).findAny().get().getGoods().size(), 2);

        // add to shopping cart
        GoodsInShoppingCart goods = new GoodsInShoppingCart();
        goods.setId(1L);
        goods.setNumber(10);
        HttpResponse<String> postResponse = getAddToShoppingCartResponse(goods);
        Response<ShoppingCartResponse> postResponseParsedResult = parseJsonObject(postResponse.body(), new TypeReference<>() {
        });
        assertEquals(postResponse.statusCode(), HttpStatus.OK.value());
        assertEquals(postResponseParsedResult.getData().getShop().getId(), 11L);
        assertEquals(postResponseParsedResult.getData().getGoods().stream().filter(data -> data.getId() == 1L).findAny().get().getNumber(), 15);

        // delete item in shopping cart
        HttpResponse<String> deleteResponse = getDeleteItemInShoppingCartResponse(1L);
        Response<ShoppingCartResponse> deleteResponseParsedResult = parseJsonObject(deleteResponse.body(), new TypeReference<>() {
        });
        assertEquals(deleteResponse.statusCode(), HttpStatus.OK.value());
        assertEquals(deleteResponseParsedResult.getData().getGoods().size(), 2);
        assertFalse(deleteResponseParsedResult.getData().getGoods().stream().anyMatch(goodsInShoppingCart -> goodsInShoppingCart.getId() == 1L));
    }
}
