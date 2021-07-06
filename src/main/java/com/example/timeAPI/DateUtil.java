package com.example.timeAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * 针对这个问题，其实有多种解决策略：
 * • 将SimpleDateFormat作为局部变量
 * • 使用ThreadLocal（本质和上面一样）
 * • 加锁
 */
public final class DateUtil {

    private DateUtil() {
    }

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> threadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DEFAULT_DATE_PATTERN));

    public static SimpleDateFormat getFormatter(String pattern) {
        SimpleDateFormat simpleDateFormat = threadLocal.get();
        simpleDateFormat.applyPattern(getPattern(pattern));
        return simpleDateFormat;
    }

    public static SimpleDateFormat getFormatter() {
        return threadLocal.get();
    }

    private static String getPattern(String pattern) {
        if (pattern != null && !"".equals(pattern)) {
            return pattern;
        }
        return DEFAULT_DATE_PATTERN;
    }


    /**
     * 测试案例，分别测试
     * 1.多线程共用一个SimpleDateFormat进行parse，会抛异常
     * 2.多线程内各自new SimpleDateFormat，不会抛异常
     * 3.引入ThreadLocal
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        testException();
//        testFormatterWithNew();
//        testFormatterWithTL();
    }


    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void testException() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Date parse = formatter.parse("2020-12-05 16:40:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void testFormatterWithNew() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date parse = formatter.parse("2020-12-05 16:40:00");
                    countDownLatch.countDown();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) + "毫秒");
    }

    private static void testFormatterWithTL() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                try {
                    Date parse = DateUtil.getFormatter().parse("2020-12-05 16:40:00");
                    countDownLatch.countDown();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) + "毫秒");
    }
}