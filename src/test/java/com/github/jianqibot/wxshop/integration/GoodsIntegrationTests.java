package com.github.jianqibot.wxshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jianqibot.wxshop.WxShopApplication;
import com.github.jianqibot.wxshop.api.OrderService;
import com.github.jianqibot.wxshop.entity.LoginResponse;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.entity.Response;
import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.generate.Shop;
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

import static com.github.jianqibot.wxshop.service.TelVerificationServiceTest.VALID_PARAM;
import static com.github.jianqibot.wxshop.service.TelVerificationServiceTest.VALID_PARAM_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})

public class GoodsIntegrationTests extends TestHelper {
    @Autowired
    private OrderService orderService;

    @Test
    @Order(1)
    public void GoodsTest() throws IOException, InterruptedException {
        // register
        getRegisterResponse(VALID_PARAM);
        // login
        getLoginResponse(VALID_PARAM_CODE);
        // get user info from login-status
        HttpResponse<String> loginStatusResponse = getLoginStatusResponse();
        LoginResponse loginResponse = parseJsonObject(loginStatusResponse.body(), new TypeReference<>() {
        });

        Long userId = loginResponse.getUser().getId();
        // mock shop & create shop
        HttpResponse<String> createShopResponse = getCreateShopResponse(mockShop());
        Response<Shop> createShopResponseData = parseJsonObject(createShopResponse.body(), new TypeReference<>() {
        });
        Long shopId = createShopResponseData.getData().getId();
        //  mock goods & crate goods
        Goods mockGoods = mockGoods(shopId);
        HttpResponse<String> createGoodsResponse = getCreateGoodsResponse(mockGoods);
        Response<Goods> createGoodsResponseData = parseJsonObject(createGoodsResponse.body(), new TypeReference<>() {
        });
        // check result
        Long goodsId = createGoodsResponseData.getData().getId();
        assertEquals(HttpStatus.CREATED.value(), createGoodsResponse.statusCode());
        assertEquals(createGoodsResponseData.getData().getName(), mockGoods.getName());
        assertEquals(createGoodsResponseData.getData().getDescription(), mockGoods.getDescription());
        //assertEquals(createGoodsResponseData.getData().getDetails(), mockGoods.getDetails());
        assertEquals(createGoodsResponseData.getData().getImgUrl(), mockGoods.getImgUrl());
        assertEquals(createGoodsResponseData.getData().getPrice(), mockGoods.getPrice());
        assertEquals(createGoodsResponseData.getData().getStock(), mockGoods.getStock());
        assertEquals(createGoodsResponseData.getData().getShopId(), shopId);

        // updateGoodsTest
        Goods updateGoods = mockUpdatedGoods();
        HttpResponse<String> updateGoodsResponse = getUpdateGoodsResponse(updateGoods, goodsId);
        Response<Goods> updateGoodsResponseData = parseJsonObject(updateGoodsResponse.body(), new TypeReference<>() {
        });
        assertEquals(updateGoodsResponseData.getData().getPrice(), updateGoods.getPrice());
        assertEquals(updateGoodsResponseData.getData().getStock(), updateGoods.getStock());

        // getGoodsTest
        HttpResponse<String> getGoodsResponse = getGetGoodsResponse(1, 10, shopId);
        PageResponse<Goods> getGoodsResponseData = parseJsonObject(getGoodsResponse.body(), new TypeReference<>() {
        });
        assertEquals(getGoodsResponseData.getData().get(0).getPrice(), updateGoods.getPrice());
        assertEquals(getGoodsResponseData.getData().get(0).getStock(), updateGoods.getStock());

        // deleteGoodsTest
        // login
/*        HttpResponse<String> deleteGoodsResponse = getDeleteGoodsResponse(goodsId);
        Response<Goods> getDeleteGoodsResponseData = parseJsonObject(deleteGoodsResponse.body(), new TypeReference<>() {
        });
        assertEquals(deleteGoodsResponse.statusCode(), HttpStatus.NO_CONTENT.value());
        assertEquals(getDeleteGoodsResponseData.getData().getName(), mockGoods.getName());
        assertEquals(getDeleteGoodsResponseData.getData().getDescription(), mockGoods.getDescription());
        assertEquals(getDeleteGoodsResponseData.getData().getDetails(), mockGoods.getDetails());
        assertEquals(getDeleteGoodsResponseData.getData().getImgUrl(), mockGoods.getImgUrl());
        assertEquals(getDeleteGoodsResponseData.getData().getPrice(), updateGoods.getPrice());
        assertEquals(getDeleteGoodsResponseData.getData().getStock(), updateGoods.getStock());
        assertEquals(getDeleteGoodsResponseData.getData().getShopId(), shopId);*/
    }


    private Shop mockShop() {
        Shop shop = new Shop();
        shop.setId(123L);
        shop.setName("my shop");
        shop.setDescription("my soap shop");
        shop.setImgUrl("https://img.url/shop");
        return shop;
    }

    private Goods mockGoods(Long shopId) {
        Goods goods = new Goods();
        goods.setName("soap");
        goods.setDescription("coconut soap");
        goods.setDetails("coconut soap made with premium material");
        goods.setImgUrl("https://img.url/goods");
        goods.setPrice(500L);
        goods.setStock(10);
        goods.setShopId(shopId);
        return goods;
    }

    private Goods mockUpdatedGoods() {
        Goods goods = new Goods();
        goods.setName("soap");
        goods.setDescription("coconut soap");
        goods.setDetails("coconut soap made with premium material");
        goods.setImgUrl("https://img.url/goods");
        goods.setPrice(400L);
        goods.setStock(20);
        return goods;
    }
}
