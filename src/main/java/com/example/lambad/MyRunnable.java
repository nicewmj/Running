package com.example.lambad;

/**
 * 也就是说，如果你希望一个接口能接收Lambda表达式充当匿名类对象，那么接口必须仅有一个抽象方法，
 * 这是函数式接口的定义。通常我们可以在接口上加一个@FunctionalInterface检测，作用于@Override一样。
 * 但函数式接口和@FunctionalInterface注解没有必然联系。
 */
@FunctionalInterface //所谓函数式接口，最核心的特征是：有且只有一个抽象方法。
public interface MyRunnable {
    void run();
//    void run1();
}