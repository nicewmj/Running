package com.example.spring监听机制.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * 短信服务，监听OrderSuccessEvent
 *
 * SmsService既是一个服务，还是一个Listener，因为它既有@Service又实现了ApplicationListener接口。
 * 但是仅仅是为了一个监听回调方法而实现一个接口，未免麻烦，所以Spring提供了注解的方式：
 */
@Service
//public class SmsService implements ApplicationListener<OrderSuccessEvent> {
public class SmsService{


    /**
     * 发送短信
     */
    @EventListener(OrderSuccessEvent.class)//Spring提供了注解的方式：监听回调方法
    public void sendSms() {
        try {
            Thread.sleep(1000L * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送短信...");
    }
}