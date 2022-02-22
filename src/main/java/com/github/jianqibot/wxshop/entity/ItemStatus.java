package com.github.jianqibot.wxshop.entity;

public enum ItemStatus {
    OK(),
    DELETED();

    public String getName() {
        return name().toLowerCase();
    }
}
