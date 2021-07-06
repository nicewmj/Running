package com.example.spring监听机制.demo;

import org.springframework.context.ApplicationEvent;

public class OrderSuccessEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OrderSuccessEvent(Object source) {
        super(source);
    }
}