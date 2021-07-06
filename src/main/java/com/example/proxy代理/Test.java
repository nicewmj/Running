package com.example.proxy代理;

public class Test {
    public static void main(String[] args) {
        // 把目标对象通过构造器塞入代理对象
        Calculator calculator = new CalculatorProxy(new CalculatorImpl());
        // 代理对象调用目标对象方法完成计算，并在前后打印日志
        calculator.add(1, 2);
        calculator.subtract(2, 1);
    }
}  