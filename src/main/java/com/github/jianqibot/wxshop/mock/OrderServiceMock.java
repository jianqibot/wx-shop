package com.github.jianqibot.wxshop.mock;

import com.github.jianqibot.wxshop.api.OrderService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "${wxshop.orderService.version}")
public class OrderServiceMock implements OrderService {
    @Override
    public void placeOrder(int goodsId, int number) {
        System.out.println("I am a mock");
    }
}
