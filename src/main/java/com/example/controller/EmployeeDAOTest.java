package com.example.controller;

import com.example.annotation.MyAfter;
import com.example.annotation.MyBefore;
import com.example.annotation.MyTest;

/**
 * 和我们平时使用Junit测试时一样
 *
 * @author qiyu
 */
public class EmployeeDAOTest {

    @MyBefore
    public void init() {
        System.out.println("初始化...");
    }

    @MyAfter
    public void destroy() {
        System.out.println("销毁...");
    }

    @MyTest
    public void testSave() {
        System.out.println("save...");
    }

    @MyTest
    public void testDelete() {
        System.out.println("delete...");
    }
}
