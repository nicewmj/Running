package com.example.lambad;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class Test {
    /**
     * 测试 Stream API的效率问题
     *
     * @param args
     */
    public static void main(String[] args) {

        // 1. 简单数据类型：整数
        testSimpleType();

        // 2. 复杂数据类型：对象
//        testObjectType();

    }

    private static void testSimpleType() {
        Random random = new Random();
        List<Integer> integerList = new ArrayList<>();

        for (int i = 0; i < 10000000; i++) {
            integerList.add(random.nextInt(Integer.MAX_VALUE));
        }

        // 1) stream
        testStream(integerList);
        // 2) parallelStream
        testParallelStream(integerList);
        // 3) 普通for
        testForLoop(integerList);
        // 4) 增强型for
        testStrongForLoop(integerList);
        // 5) 迭代器
        testIterator(integerList);
    }

    private static void testObjectType() {
        Random random = new Random();
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < 10000000; i++) {
            productList.add(new Product("pro" + i, i, random.nextInt(Integer.MAX_VALUE)));
        }

        // 1) stream
        testProductStream(productList);
        // 2) parallelStream
        testProductParallelStream(productList);
        // 3) 普通for
        testProductForLoop(productList);
        // 4) 增强型for
        testProductStrongForLoop(productList);
        // 5) 迭代器
        testProductIterator(productList);
    }

    // -------- 测试简单类型 --------
    public static void testStream(List<Integer> list) {
        long start = System.currentTimeMillis();

        Optional<Integer> optional = list.stream().max(Integer::compare);
        System.out.println("result=" + optional.orElse(0));

        long end = System.currentTimeMillis();
        System.out.println("testStream耗时:" + (end - start) + "ms");
    }

    public static void testParallelStream(List<Integer> list) {
        long start = System.currentTimeMillis();

        Optional<Integer> optional = list.parallelStream().max(Integer::compare);
        System.out.println("result=" + optional.orElse(0));

        long end = System.currentTimeMillis();
        System.out.println("testParallelStream耗时:" + (end - start) + "ms");
    }

    public static void testForLoop(List<Integer> list) {
        long start = System.currentTimeMillis();

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);
            if (current > max) {
                max = current;
            }
        }
        System.out.println("result=" + max);

        long end = System.currentTimeMillis();
        System.out.println("testForLoop耗时:" + (end - start) + "ms");
    }

    public static void testStrongForLoop(List<Integer> list) {
        long start = System.currentTimeMillis();

        int max = Integer.MIN_VALUE;
        for (Integer integer : list) {
            if (integer > max) {
                max = integer;
            }
        }
        System.out.println("result=" + max);

        long end = System.currentTimeMillis();
        System.out.println("testStrongForLoop耗时:" + (end - start) + "ms");
    }

    public static void testIterator(List<Integer> list) {
        long start = System.currentTimeMillis();

        Iterator<Integer> it = list.iterator();
        int max = it.next();

        while (it.hasNext()) {
            int current = it.next();
            if (current > max) {
                max = current;
            }
        }
        System.out.println("result=" + max);

        long end = System.currentTimeMillis();
        System.out.println("testIterator耗时:" + (end - start) + "ms");
    }


    // -------- 测试对象类型 --------
    public static void testProductStream(List<Product> list) {
        long start = System.currentTimeMillis();

        Optional<Product> optional = list.stream().max((p1, p2) -> p1.hot - p2.hot);
        System.out.println(optional.orElseThrow(() -> new RuntimeException("对象不存在")));

        long end = System.currentTimeMillis();
        System.out.println("testProductStream耗时:" + (end - start) + "ms");
    }

    public static void testProductParallelStream(List<Product> list) {
        long start = System.currentTimeMillis();

        Optional<Product> optional = list.stream().max((p1, p2) -> p1.hot - p2.hot);
        System.out.println(optional.orElseThrow(() -> new RuntimeException("对象不存在")));

        long end = System.currentTimeMillis();
        System.out.println("testProductParallelStream耗时:" + (end - start) + "ms");
    }

    public static void testProductForLoop(List<Product> list) {
        long start = System.currentTimeMillis();

        Product maxHot = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            Product current = list.get(i);
            if (current.hot > maxHot.hot) {
                maxHot = current;
            }
        }
        System.out.println(maxHot);

        long end = System.currentTimeMillis();
        System.out.println("testProductForLoop耗时:" + (end - start) + "ms");
    }

    public static void testProductStrongForLoop(List<Product> list) {
        long start = System.currentTimeMillis();

        Product maxHot = list.get(0);
        for (Product product : list) {
            if (product.hot > maxHot.hot) {
                maxHot = product;
            }
        }
        System.out.println(maxHot);

        long end = System.currentTimeMillis();
        System.out.println("testProductStrongForLoop耗时:" + (end - start) + "ms");
    }

    public static void testProductIterator(List<Product> list) {
        long start = System.currentTimeMillis();

        Iterator<Product> it = list.iterator();
        Product maxHot = it.next();

        while (it.hasNext()) {
            Product current = it.next();
            if (current.hot > maxHot.hot) {
                maxHot = current;
            }
        }
        System.out.println(maxHot);

        long end = System.currentTimeMillis();
        System.out.println("testProductIterator耗时:" + (end - start) + "ms");
    }

}

@Data
@AllArgsConstructor
class Product {
    // 名称
    String name;
    // 库存
    Integer stock;
    // 热度
    Integer hot;
}