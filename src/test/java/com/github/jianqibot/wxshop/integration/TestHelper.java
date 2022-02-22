package com.github.jianqibot.wxshop.integration;

import com.github.jianqibot.wxshop.entity.GoodsInShoppingCart;
import com.github.jianqibot.wxshop.entity.TelAndCode;
import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.generate.Shop;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class TestHelper extends SendHttpRequestAndGetHttpResponse {

    // auth
    public HttpResponse<String> getRegisterResponse(TelAndCode param) throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/code", param);
    }

    // auth
    public HttpResponse<String> getLoginStatusResponse() throws IOException, InterruptedException {
        return sendHttpGetRequestAndGetHttpResponse("/api/status");
    }

    // auth
    public HttpResponse<String> getLoginResponse(TelAndCode param) throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/login", param);
    }

    // auth
    public HttpResponse<String> getLogoutResponse() throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/logout", null);
    }

    // auth
    public String getCookie(TelAndCode param) throws IOException, InterruptedException {
        getRegisterResponse(param);
        HttpResponse<String> loginResponse = getLoginResponse(param);
        List<String> setCookie = loginResponse.headers().allValues("Set-Cookie");
        return getSessionIDFromSetCookie(setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID")).findFirst().get());
    }

    // goods
    public HttpResponse<String> getCreateGoodsResponse(Goods param) throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/v1/goods", param);
    }

    // goods
    public HttpResponse<String> getUpdateGoodsResponse(Goods param, Long goodsId) throws IOException, InterruptedException {
        return sendHttpPutRequestAndGetHttpResponse("/api/v1/goods/" + goodsId, param);
    }

    // goods
    public HttpResponse<String> getGetGoodsResponse(Integer pageNum, Integer pageSize, Long shopId) throws IOException, InterruptedException {
        return sendHttpGetRequestAndGetHttpResponse("/api/v1/goods?pageNum=" + pageNum + "&pageSize=" + pageSize + "&shopId=" + shopId);
    }

    // goods
    public HttpResponse<String> getDeleteGoodsResponse(Long goodsId) throws IOException, InterruptedException {
        HttpResponse<String> s = sendHttpDeleteRequestAndGetHttpResponse("/api/v1/goods/" + goodsId);
        return s;
    }

    // shop
    public HttpResponse<String> getCreateShopResponse(Shop param) throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/v1/shop", param);
    }

    //shopping cart
    public HttpResponse<String> getGetShoppingCartByUserIdResponse(Integer pageNum, Integer pageSize) throws IOException, InterruptedException {
        return sendHttpGetRequestAndGetHttpResponse("/api/v1/shoppingCart?pageNum=" + pageNum + "&pageSize=" + pageSize);
    }

    // shopping cart
    public HttpResponse<String> getAddToShoppingCartResponse(GoodsInShoppingCart param) throws IOException, InterruptedException {
        return sendHttpPostRequestAndGetHttpResponse("/api/v1/shoppingCart", param);
    }

    //
    // shopping cart
    public HttpResponse<String> getDeleteItemInShoppingCartResponse(Long goodsId) throws IOException, InterruptedException {
        return sendHttpDeleteRequestAndGetHttpResponse("/api/v1/shoppingCart/" + goodsId);
    }

    private String getSessionIDFromSetCookie(String setCookie) {
        int semicolonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semicolonIndex);
    }
}
