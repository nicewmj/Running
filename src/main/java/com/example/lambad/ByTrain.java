package com.example.lambad;

public class ByTrain implements MyRunnable {
    @Override
    public void run() {
        System.out.println("去12306买了一张票");
        System.out.println("坐火车...");
    }
}