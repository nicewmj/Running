package com.example.lambad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Demo {
    public static void main(String[] args) {
//        new MyThread(() -> {
//            System.out.println("不用买票");
//            System.out.println("骑电瓶车...");
//        }).start();
//    }

//            String str1 = "abc";
//            String str2 = "abcd";
//            int i = compareString(str1, str2, Comparator.comparingInt(String::length));

        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> collect = list.stream().filter(s -> s > 2).collect(Collectors.toList());
        System.out.println(collect);


    }

    public static int compareString(String str1, String str2, Comparator<String> comparator) {
        return comparator.compare(str1, str2);
    }
}