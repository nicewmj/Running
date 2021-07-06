package com.example.spring监听机制.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 物流服务
 */
@Service
public class CarService  implements ApplicationListener<OrderSuccessEvent> {
    @Override
    public void onApplicationEvent(OrderSuccessEvent event) {
        this.dispatch();
    }

    public void dispatch() {
        System.out.println("发车咯...");
    }
}