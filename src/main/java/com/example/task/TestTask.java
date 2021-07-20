package com.example.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 其实，SpringBoot的定时任务是很鸡肋的：
     * • 不支持集群时单节点启动（同事使用Redis分布式锁就是为了解决避免多节点重复执行）
     * • 不支持分片任务
     * • 不支持失败重试（一个任务失败了就失败了，不会重试）
     * • 动态调整比较繁琐（我曾经做过一个项目，要求前端页面可以动态配置任务启动的时间点）
     * • ...
 *      所以，对于多节点部署或者分布式项目，还是乖乖用Elastic-Job或者XXL-Job吧。
 *
 * SpringBoot定时任务   SpringBoot定时任务默认单线程
 *
 * 但由于每个任务执行时间短且一般需求对时间准确度要求并不特别严格，串行执行慢个2~3秒都可以接受。（有时可能会定时任务执行不了，死锁
 * 配置线程池 可解决
 *      如果仅仅想要增加定时任务线程池的线程数，可以直接在配置文件中更改
 * @EnableScheduling + @Scheduled两个注解即可启用定时任务。
 * @EnableScheduling放在Application启动类上也可以，方便整体把控，有可能随着项目的开发，会出现多个定时任务类。
 */
@Slf4j
@Component
public class TestTask {
//      */number  表示“每隔...”，是最实用的
//      逗号      表示“或”，比如 8,13,18 表示 8或13或18
//      比如，在[秒]的位置写上 */10，表示每隔10秒执行一次
    /**
     * @Schedule(cron = "秒 分 时 日 月 星期 [年]") 参数的含义
     *
     * cron ：定时执行（最常用）
     * fixedDelay =5*1000L  ；距离上一次任务结束的时间  五秒  fixDelay：用于指定上一个任务结束与下一个任务开始的时间间隔
     * fixedRate：距离上一次开始的时间                         fixRate：用于指定上一个任务开始与下一个任务开始的时间间隔
     * initialDelay：启动多少秒后开始首次任务
     *
     *
     */
//    @Async("taskExecutor")
//    @Scheduled(cron = "*/10 * * * * ?")
    public void test1() {
        log.info("=========test1任务启动============");
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("=========test1任务结束============");
    }

//    @Async("taskExecutor")
//    @Scheduled(cron = "*/10 * * * * ?")
    public void test2() {
        log.info("=========test2任务启动============");
        try {
            // 模拟远程服务卡死
            Thread.sleep(1000 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("=========test2任务结束============");
    }

//    @Async("taskExecutor")
//    @Scheduled(cron = "*/10 * * * * ?")
    public void test3() {
        log.info("=========test3任务启动============");
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("=========test3任务结束============");
    }
}
