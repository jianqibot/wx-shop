package com.github.jianqibot.wxshop.controller;

import com.github.jianqibot.wxshop.api.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    @DubboReference(version = "${wxshop.orderService.version}",
                    url = "${wxshop.orderService.url}")
    private OrderService orderService;

    @RequestMapping("testRpc")
    public String testRpc() {
        orderService.placeOrder(1, 10);
        return "testRpc";
    }
}
