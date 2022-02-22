package com.github.jianqibot.wxshop.entity;

import com.github.jianqibot.wxshop.generate.Goods;

public class GoodsInShoppingCart extends Goods {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
