package com.example.spring监听机制.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * spring 的异步通知机制配置
 *
 * 当SimpleApplicationEventMulticaster中的Executor不为null，就会执行异步通知。
 * 最后说一句，Spring事件机制适合单体应用，同一个JVM且并发不大的情况，
 * 如果是分布式应用，推荐使用MQ。Spring事件监听机制和MQ有相似的地方，也有不同的地方。
 * MQ允许跨JVM，因为它本身是独立于项目之外的，切提供了消息持久化的特性，而Spring事件机制哪怕使用了异步，
 * 本质是还是一种方法调用，宕机了就没了。
 */
@Configuration
public class AsyncEventConfig {

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

}