package com.example.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.*;

/**
 * 线程池配置
 * @author qiyu
 * 从源码可以看出来，SpringBoot对外暴露了三种自定义定时任务线程池的方法：
 * • 如果存在SchedulingConfigurer类型的Bean，用SchedulingConfigurer
 * • 如果存在TaskScheduler类型的Bean，用TaskScheduler
 * • 如果存在ScheduledExecutorService类型的Bean，用ScheduledExecutorService
 * • 以上都没有，就用默认的,沿用registrar内部默认的scheduler（单线程）
 *
 */
/*
@Slf4j
@EnableAsync // 来了，这里挖了一个坑 异步执行，看起来不是串行，其实还是个单线程的（去除，不需要）
@Configuration
public class ThreadPoolTaskConfig implements WebMvcConfigurer {

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(5);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("async-task-");

        */
/*
            SpringBoot默认提供了4种拒绝策略
            这4种拒绝策略被定义在ThreadPoolExecutor类的内部，是静态内部类。
            • CallerRunsPolicy：不使用异步线程，直接主线程执行
            • AbortPolicy：丢弃当前任务，直接抛异常
            • DiscardPolicy：丢弃当前任务，无声无息（不抛异常）
            • DiscardOldestPolicy：丢弃队列里最老的任务，执行当前任务
         *//*


        */
/*
        解决方案1：
        实际发现线程池不够用了，你直接跑主线程吗？还记得Tomcat被卡爆的案例吗？直接丢弃？你确定对业务没影响吗？
        如果业务本身不在乎请求失败，那是没关系的，否则丢弃策略就不合适了。
        一般可以选择在丢弃策略里使用MQ（延后缓冲）或者发邮件告警（及时发现），只要实现RejectedExecutionHandler接口即可：
         *//*

        // 线程池对拒绝任务的处理策略
       executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
           @Override
           public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
               log.info("发送告警邮件======>:嘿沙雕，线上定时任务卡爆了, 当前线程名称为:{}, 当前线程池队列长度为:{}",r.toString(),executor.getQueue().size());
           }
       });
        // 初始化
        executor.initialize();
        return executor;
    }
*/

/**
 * 方式1：重写SchedulingConfigurer#configureTasks()
 */
@Slf4j
@Configuration
public class ThreadPoolTaskConfig implements WebMvcConfigurer {

    // 方式1: SchedulingConfigurer 其实内部还是设置一个TaskScheduler
    @Bean
    public SchedulingConfigurer schedulingConfigurer() {
        return new MySchedulingConfigurer();
    }

    static class MySchedulingConfigurer implements SchedulingConfigurer {

        @Override
        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            taskScheduler.setPoolSize(3);
            taskScheduler.setThreadNamePrefix("schedule-task-");
            taskScheduler.setRejectedExecutionHandler(
                    new RejectedExecutionHandler() {
                        /*
                         * 自定义线程池拒绝策略（模拟发送告警邮件）
                         */
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            log.info("发送告警邮件======>:嘿沙雕，线上定时任务卡爆了, 当前线程名称为:{}, 当前线程池队列长度为:{}",
                                    r.toString(),
                                    executor.getQueue().size());
                        }
                    });
            taskScheduler.initialize();
            taskRegistrar.setScheduler(taskScheduler);
        }
    }
}

    /**
     * 方式2：@Bean + ThreadPoolTaskScheduler
     */
/*
@Slf4j
@Configuration
public class ThreadPoolTaskConfig implements WebMvcConfigurer {

    // 方式2: taskScheduler
    @Bean("taskScheduler")
    public Executor taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.setThreadNamePrefix("schedule-task-");
        taskScheduler.setRejectedExecutionHandler(
                new RejectedExecutionHandler() {
                    */
/**
                     * 自定义线程池拒绝策略（模拟发送告警邮件）
                     *//*

                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.info("发送告警邮件======>:嘿沙雕，线上定时任务卡爆了, 当前线程名称为:{}, 当前线程池队列长度为:{}",
                                r.toString(),
                                executor.getQueue().size());
                    }
                });
        taskScheduler.initialize();
        return taskScheduler;
    }
}
*/


/**
 * 方式3：@Bean + ScheduledThreadPoolExecutor
 * 这个方法有问题 启动保存
 * The bean 'taskScheduler', defined in class path resource [org/springframework/boot/autoconfigure/task/TaskSchedulingAutoConfiguration.class],
 * could not be registered. A bean with that name has already been defined in class path resource [com/example/task/ThreadPoolTaskConfig.class] and overriding is disabled.
 */
/*
@Slf4j
@Configuration
public class ThreadPoolTaskConfig implements WebMvcConfigurer {

    // 方式3
    @Bean("taskScheduler")
    public Executor taskScheduler() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                3,
                new RejectedExecutionHandler() {
                    */
/**
                     * 自定义线程池拒绝策略（模拟发送告警邮件）
                     *//*

                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.info("发送告警邮件======>:嘿沙雕，线上定时任务卡爆了, 当前线程名称为:{}, 当前线程池队列长度为:{}",
                                r.toString(),
                                executor.getQueue().size());
                    }
                }
        );
        executor.setMaximumPoolSize(5);
        executor.setKeepAliveTime(60, TimeUnit.SECONDS);
        return executor;
    }

}*/
