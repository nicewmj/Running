package com.example.proxy代理;

/**
 * 静态代理类，实现Calculator接口
 */
public class CalculatorProxy implements Calculator {
    // 代理对象内部维护一个目标对象引用
    private Calculator target;

    // 通过构造方法，传入目标对象
    public CalculatorProxy(Calculator target) {
        this.target = target;
    }

    // 调用目标对象的add，并在前后打印日志
    @Override
    public int add(int a, int b) {
        System.out.println("add方法开始...");
        int result = target.add(a, b);
        System.out.println("add方法结束...");
        return result;
    }

    // 调用目标对象的subtract，并在前后打印日志
    @Override
    public int subtract(int a, int b) {
        System.out.println("subtract方法开始...");
        int result = target.subtract(a, b);
        System.out.println("subtract方法结束...");
        return result;
    }

    // 乘法、除法...
}
