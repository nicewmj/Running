package com.example.threadTest;

import java.util.ArrayList;
import java.util.concurrent.*;

public class AsyncAndWaitTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<String> runnable =  new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "========>正在执行1");
                try {
                    Thread.sleep(3*1000L);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }  ,"success");


        // 第一次看到FutureTask时，相信大家会震惊：啥玩意，怎么把Callable往FurtureTask里塞呢？！
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "========>正在执行");
                try {
                    Thread.sleep(3 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "success";
            }
        });

        System.out.println(Thread.currentThread().getName() + "========>启动任务");

        // 看到这，你再次震惊：啥玩意，怎么又把FutureTask塞到Thread里了呢？！
        new Thread(futureTask).start();

        // 可以获取异步结果（会阻塞3秒）
        String result = futureTask.get();
        System.out.println("任务执行结束，result====>" + result);
        ArrayBlockingQueue a =   new ArrayBlockingQueue(1,true,new ArrayList());
    }

}