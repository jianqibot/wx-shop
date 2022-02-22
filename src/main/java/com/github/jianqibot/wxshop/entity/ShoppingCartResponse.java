package com.github.jianqibot.wxshop.entity;

import com.github.jianqibot.wxshop.generate.Shop;

import java.util.List;

public class ShoppingCartResponse {
    private Shop shop;
    private List<GoodsInShoppingCart> goods;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<GoodsInShoppingCart> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInShoppingCart> goods) {
        this.goods = goods;
    }
}
