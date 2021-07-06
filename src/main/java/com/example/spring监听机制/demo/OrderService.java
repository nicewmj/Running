package com.example.spring监听机制.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 订单服务
 */
@Service
public class OrderService {

    @Autowired
    private ApplicationContext applicationContext;

    public void order() {
        // 下单成功
        System.out.println("下单成功...");
        // 发布通知（传入了当前对象）
        applicationContext.publishEvent(new OrderSuccessEvent(this));
        System.out.println("main线程结束...");

        try {
            Thread.sleep(1000L * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
}