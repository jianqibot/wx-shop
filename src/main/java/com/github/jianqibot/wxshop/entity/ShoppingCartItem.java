package com.github.jianqibot.wxshop.entity;

import com.github.jianqibot.wxshop.generate.Shop;

public class ShoppingCartItem {
    private Shop shop;
    private GoodsInShoppingCart goods;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public GoodsInShoppingCart getGoods() {
        return goods;
    }

    public void setGoods(GoodsInShoppingCart goods) {
        this.goods = goods;
    }
}
